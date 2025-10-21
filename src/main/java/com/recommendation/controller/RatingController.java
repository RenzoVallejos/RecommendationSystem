package com.recommendation.controller;

import com.recommendation.entity.Rating;
import com.recommendation.entity.Movie;
import com.recommendation.entity.User;
import com.recommendation.repository.RatingRepository;
import com.recommendation.repository.MovieRepository;
import com.recommendation.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
@Tag(name = "Ratings", description = "Rating CRUD operations")
public class RatingController {
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MovieRepository movieRepository;
    
    @GetMapping
    @Operation(summary = "Get all ratings", description = "Retrieve all ratings in the database")
    public ResponseEntity<List<Rating>> getAllRatings() {
        return ResponseEntity.ok(ratingRepository.findAll());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get rating by ID", description = "Retrieve a specific rating by its ID")
    public ResponseEntity<Rating> getRatingById(
            @Parameter(description = "Rating ID", example = "1")
            @PathVariable Long id) {
        Optional<Rating> rating = ratingRepository.findById(id);
        return rating.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get ratings by user", description = "Retrieve all ratings by a specific user")
    public ResponseEntity<List<Rating>> getRatingsByUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId) {
        List<Rating> ratings = ratingRepository.findByUserId(userId);
        return ResponseEntity.ok(ratings);
    }
    
    @PostMapping
    @Operation(summary = "Create rating", description = "Add a new rating to the database")
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());
        Optional<Movie> movie = movieRepository.findById(request.getMovieId());
        
        if (user.isPresent() && movie.isPresent()) {
            Rating rating = new Rating(user.get(), movie.get(), request.getScore());
            Rating savedRating = ratingRepository.save(rating);
            return ResponseEntity.ok(savedRating);
        }
        return ResponseEntity.badRequest().build();
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update rating", description = "Update an existing rating")
    public ResponseEntity<Rating> updateRating(
            @Parameter(description = "Rating ID", example = "1")
            @PathVariable Long id, 
            @RequestBody RatingRequest request) {
        Optional<Rating> optionalRating = ratingRepository.findById(id);
        if (optionalRating.isPresent()) {
            Rating rating = optionalRating.get();
            rating.setScore(request.getScore());
            return ResponseEntity.ok(ratingRepository.save(rating));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete rating", description = "Remove a rating from the database")
    public ResponseEntity<Void> deleteRating(
            @Parameter(description = "Rating ID", example = "1")
            @PathVariable Long id) {
        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    public static class RatingRequest {
        private Long userId;
        private Long movieId;
        private Double score;
        
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        
        public Long getMovieId() { return movieId; }
        public void setMovieId(Long movieId) { this.movieId = movieId; }
        
        public Double getScore() { return score; }
        public void setScore(Double score) { this.score = score; }
    }
}