package com.recommendation.controller;

import com.recommendation.entity.Movie;
import com.recommendation.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    
    @Autowired
    private SearchService searchService;
    
    @GetMapping
    public ResponseEntity<List<Movie>> search(@RequestParam String query) {
        List<Movie> results = searchService.searchMovies(query);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/async")
    public CompletableFuture<ResponseEntity<List<Movie>>> searchAsync(@RequestParam String query) {
        return searchService.searchMoviesAsync(query)
                .thenApply(ResponseEntity::ok);
    }
    
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Movie>> searchByGenre(@PathVariable String genre) {
        List<Movie> results = searchService.searchByGenre(genre);
        return ResponseEntity.ok(results);
    }
}