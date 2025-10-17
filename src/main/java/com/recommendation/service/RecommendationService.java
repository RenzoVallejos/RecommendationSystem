package com.recommendation.service;

import com.recommendation.entity.Movie;
import com.recommendation.entity.Rating;
import com.recommendation.repository.MovieRepository;
import com.recommendation.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Cacheable(value = "userRecommendations", key = "#userId")
    public List<Movie> getRecommendationsForUser(Long userId) {
        // Collaborative filtering: find similar users
        List<Rating> userRatings = ratingRepository.findByUserId(userId);
        if (userRatings.isEmpty()) {
            return getPopularMovies();
        }
        
        Set<Long> userMovieIds = userRatings.stream()
                .map(r -> r.getMovie().getId())
                .collect(Collectors.toSet());
        
        // Find users with similar taste
        List<Long> similarUsers = findSimilarUsers(userId, userRatings);
        
        // Get movies liked by similar users that current user hasn't seen
        Map<Long, Double> movieScores = new HashMap<>();
        for (Long similarUserId : similarUsers) {
            List<Rating> similarUserRatings = ratingRepository.findByUserId(similarUserId);
            for (Rating rating : similarUserRatings) {
                Long movieId = rating.getMovie().getId();
                if (!userMovieIds.contains(movieId) && rating.getScore() >= 4.0) {
                    movieScores.merge(movieId, rating.getScore(), Double::sum);
                }
            }
        }
        
        // Sort by score and return top recommendations
        return movieScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(10)
                .map(entry -> movieRepository.findById(entry.getKey()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    @Async
    public CompletableFuture<List<Movie>> getRecommendationsForUserAsync(Long userId) {
        List<Movie> recommendations = getRecommendationsForUser(userId);
        return CompletableFuture.completedFuture(recommendations);
    }
    
    @Cacheable(value = "similarMovies", key = "#movieId")
    public List<Movie> getSimilarMovies(Long movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        if (movie.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Content-based filtering: find movies with similar genres
        List<String> genres = movie.get().getGenres();
        return movieRepository.findAll().stream()
                .filter(m -> !m.getId().equals(movieId))
                .filter(m -> hasCommonGenres(m.getGenres(), genres))
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .limit(5)
                .collect(Collectors.toList());
    }
    
    private List<Movie> getPopularMovies() {
        return movieRepository.findByRatingGreaterThanEqual(8.0);
    }
    
    private List<Long> findSimilarUsers(Long userId, List<Rating> userRatings) {
        // Simple similarity: users who rated the same movies highly
        Set<Long> userMovieIds = userRatings.stream()
                .filter(r -> r.getScore() >= 4.0)
                .map(r -> r.getMovie().getId())
                .collect(Collectors.toSet());
        
        return ratingRepository.findAll().stream()
                .filter(r -> !r.getUser().getId().equals(userId))
                .filter(r -> r.getScore() >= 4.0)
                .filter(r -> userMovieIds.contains(r.getMovie().getId()))
                .map(r -> r.getUser().getId())
                .distinct()
                .limit(5)
                .collect(Collectors.toList());
    }
    
    private boolean hasCommonGenres(List<String> genres1, List<String> genres2) {
        return genres1.stream().anyMatch(genres2::contains);
    }
}