package com.recommendation.controller;

import com.recommendation.entity.Movie;
import com.recommendation.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/recommend")
public class RecommendationController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Movie>> getRecommendations(@PathVariable Long userId) {
        List<Movie> recommendations = recommendationService.getRecommendationsForUser(userId);
        return ResponseEntity.ok(recommendations);
    }
    
    @GetMapping("/user/{userId}/async")
    public CompletableFuture<ResponseEntity<List<Movie>>> getRecommendationsAsync(@PathVariable Long userId) {
        return recommendationService.getRecommendationsForUserAsync(userId)
                .thenApply(ResponseEntity::ok);
    }
    
    @GetMapping("/similar/{movieId}")
    public ResponseEntity<List<Movie>> getSimilarMovies(@PathVariable Long movieId) {
        List<Movie> similar = recommendationService.getSimilarMovies(movieId);
        return ResponseEntity.ok(similar);
    }
}