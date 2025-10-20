package com.recommendation.controller;

import com.recommendation.entity.Movie;
import com.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/recommend")
@Tag(name = "Recommendations", description = "Movie recommendation operations")
public class RecommendationController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user recommendations", description = "Get personalized movie recommendations for a user")
    public ResponseEntity<List<Movie>> getRecommendations(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId) {
        List<Movie> recommendations = recommendationService.getRecommendationsForUser(userId);
        return ResponseEntity.ok(recommendations);
    }
    
    @GetMapping("/user/{userId}/async")
    @Operation(summary = "Get async user recommendations", description = "Get personalized recommendations asynchronously")
    public CompletableFuture<ResponseEntity<List<Movie>>> getRecommendationsAsync(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId) {
        return recommendationService.getRecommendationsForUserAsync(userId)
                .thenApply(ResponseEntity::ok);
    }
    
    @GetMapping("/similar/{movieId}")
    @Operation(summary = "Get similar movies", description = "Find movies similar to the given movie")
    public ResponseEntity<List<Movie>> getSimilarMovies(
            @Parameter(description = "Movie ID", example = "1")
            @PathVariable Long movieId) {
        List<Movie> similar = recommendationService.getSimilarMovies(movieId);
        return ResponseEntity.ok(similar);
    }
}