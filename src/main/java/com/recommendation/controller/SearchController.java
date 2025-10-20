package com.recommendation.controller;

import com.recommendation.entity.Movie;
import com.recommendation.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/search")
@Tag(name = "Search", description = "Movie search operations")
public class SearchController {
    
    @Autowired
    private SearchService searchService;
    
    @GetMapping
    @Operation(summary = "Search movies", description = "Search movies by title or description")
    public ResponseEntity<List<Movie>> search(
            @Parameter(description = "Search query", example = "matrix")
            @RequestParam String query) {
        List<Movie> results = searchService.searchMovies(query);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/async")
    @Operation(summary = "Async search movies", description = "Search movies asynchronously for better performance")
    public CompletableFuture<ResponseEntity<List<Movie>>> searchAsync(
            @Parameter(description = "Search query", example = "action")
            @RequestParam String query) {
        return searchService.searchMoviesAsync(query)
                .thenApply(ResponseEntity::ok);
    }
    
    @GetMapping("/genre/{genre}")
    @Operation(summary = "Search by genre", description = "Find movies by specific genre")
    public ResponseEntity<List<Movie>> searchByGenre(
            @Parameter(description = "Movie genre", example = "Action")
            @PathVariable String genre) {
        List<Movie> results = searchService.searchByGenre(genre);
        return ResponseEntity.ok(results);
    }
}