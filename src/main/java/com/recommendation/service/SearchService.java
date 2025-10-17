package com.recommendation.service;

import com.recommendation.entity.Movie;
import com.recommendation.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SearchService {
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Cacheable(value = "movieSearch", key = "#query")
    public List<Movie> searchMovies(String query) {
        return movieRepository.searchByTitleOrDescription(query);
    }
    
    @Async
    public CompletableFuture<List<Movie>> searchMoviesAsync(String query) {
        List<Movie> results = searchMovies(query);
        return CompletableFuture.completedFuture(results);
    }
    
    @Cacheable(value = "genreSearch", key = "#genre")
    public List<Movie> searchByGenre(String genre) {
        return movieRepository.findByGenresContaining(genre);
    }
    
    public List<Movie> getHighRatedMovies(Double minRating) {
        return movieRepository.findByRatingGreaterThanEqual(minRating);
    }
}