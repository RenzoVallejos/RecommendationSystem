package com.recommendation.service;

import com.recommendation.dto.UserRatingEvent;
import com.recommendation.entity.User;
import com.recommendation.entity.Movie;
import com.recommendation.entity.Rating;
import com.recommendation.repository.UserRepository;
import com.recommendation.repository.MovieRepository;
import com.recommendation.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventConsumerService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @KafkaListener(topics = "user-ratings", groupId = "recommendation-system")
    public void handleRatingEvent(UserRatingEvent event) {
        try {
            System.out.println("Processing rating event: " + event.getUserId() + " -> " + event.getMovieId());
            
            // Get or create user
            User user = userRepository.findById(event.getUserId())
                    .orElseGet(() -> {
                        String username = "kafka_user_" + event.getUserId();
                        return userRepository.findByUsername(username)
                                .orElseGet(() -> {
                                    User newUser = new User(username);
                                    return userRepository.save(newUser);
                                });
                    });
            
            // Get movie
            Optional<Movie> movieOpt = movieRepository.findById(event.getMovieId());
            if (movieOpt.isPresent()) {
                Movie movie = movieOpt.get();
                
                // Check if rating already exists
                Optional<Rating> existingRating = ratingRepository.findByUserIdAndMovieId(
                        event.getUserId(), event.getMovieId()).stream().findFirst();
                
                if (existingRating.isPresent()) {
                    // Update existing rating
                    Rating rating = existingRating.get();
                    rating.setScore(event.getRating());
                    ratingRepository.save(rating);
                    System.out.println("Updated rating: " + event.getRating());
                } else {
                    // Create new rating
                    Rating newRating = new Rating(user, movie, event.getRating());
                    ratingRepository.save(newRating);
                    System.out.println("Created new rating: " + event.getRating());
                }
                
                // Update movie average rating
                updateMovieAverageRating(movie);
            }
            
        } catch (Exception e) {
            System.err.println("Error processing rating event: " + e.getMessage());
        }
    }
    
    @KafkaListener(topics = "movie-views", groupId = "recommendation-system")
    public void handleViewEvent(UserRatingEvent event) {
        System.out.println("Processing view event: User " + event.getUserId() + " viewed Movie " + event.getMovieId());
        
        // In a real system, you'd track views for recommendation algorithms
        // For now, just log the event
        
        // Could update user viewing history, movie popularity scores, etc.
    }
    
    private void updateMovieAverageRating(Movie movie) {
        // Calculate new average rating for the movie
        Double avgRating = ratingRepository.findByMovieId(movie.getId())
                .stream()
                .mapToDouble(Rating::getScore)
                .average()
                .orElse(0.0);
        
        movie.setRating(Math.round(avgRating * 10.0) / 10.0); // Round to 1 decimal
        movieRepository.save(movie);
    }
}