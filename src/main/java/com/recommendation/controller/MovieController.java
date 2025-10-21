package com.recommendation.controller;

import com.recommendation.entity.Movie;
import com.recommendation.repository.MovieRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movies", description = "Movie CRUD operations")
public class MovieController {
    
    @Autowired
    private MovieRepository movieRepository;
    
    @GetMapping
    @Operation(summary = "Get all movies", description = "Retrieve all movies in the database")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieRepository.findAll());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID", description = "Retrieve a specific movie by its ID")
    public ResponseEntity<Movie> getMovieById(
            @Parameter(description = "Movie ID", example = "1")
            @PathVariable Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create movie", description = "Add a new movie to the database")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieRepository.save(movie);
        return ResponseEntity.ok(savedMovie);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update movie", description = "Update an existing movie")
    public ResponseEntity<Movie> updateMovie(
            @Parameter(description = "Movie ID", example = "1")
            @PathVariable Long id, 
            @RequestBody Movie movieDetails) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();
            movie.setTitle(movieDetails.getTitle());
            movie.setDescription(movieDetails.getDescription());
            movie.setGenres(movieDetails.getGenres());
            movie.setReleaseDate(movieDetails.getReleaseDate());
            movie.setRating(movieDetails.getRating());
            return ResponseEntity.ok(movieRepository.save(movie));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete movie", description = "Remove a movie from the database")
    public ResponseEntity<Void> deleteMovie(
            @Parameter(description = "Movie ID", example = "1")
            @PathVariable Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}