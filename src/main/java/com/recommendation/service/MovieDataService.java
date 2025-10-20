package com.recommendation.service;

import com.recommendation.entity.Movie;
import com.recommendation.entity.User;
import com.recommendation.entity.Rating;
import com.recommendation.repository.MovieRepository;
import com.recommendation.repository.UserRepository;
import com.recommendation.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieDataService {
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Transactional
    public void loadMovieLensData() {
        try {
            System.out.println("Loading MovieLens dataset...");
            
            // Load movies from CSV
            Map<Long, Movie> movies = loadMoviesFromCSV();
            System.out.println("Loaded " + movies.size() + " movies");
            
            // Load ratings and create users
            loadRatingsFromCSV(movies);
            
            System.out.println("MovieLens dataset loaded successfully!");
            System.out.println("Total movies: " + movieRepository.count());
            System.out.println("Total users: " + userRepository.count());
            System.out.println("Total ratings: " + ratingRepository.count());
            
        } catch (Exception e) {
            System.err.println("Error loading MovieLens data: " + e.getMessage());
            // Fallback to sample data
            loadSampleData();
        }
    }
    
    private Map<Long, Movie> loadMoviesFromCSV() throws Exception {
        Map<Long, Movie> movies = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("data/movies.csv").getInputStream()))) {
            
            String line;
            reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null && movies.size() < 1000) { // Limit for demo
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length >= 3) {
                    Long movieId = Long.parseLong(parts[0]);
                    String title = parts[1].replaceAll("\"", "");
                    String genres = parts[2];
                    
                    // Extract year from title
                    LocalDate releaseDate = extractYearFromTitle(title);
                    
                    // Parse genres
                    List<String> genreList = Arrays.asList(genres.split("\\|"));
                    
                    Movie movie = new Movie(title, "Movie from MovieLens dataset", genreList, releaseDate, 0.0);
                    movies.put(movieId, movie);
                }
            }
        }
        
        // Save movies in batches
        List<Movie> movieList = new ArrayList<>(movies.values());
        for (int i = 0; i < movieList.size(); i += 100) {
            int end = Math.min(i + 100, movieList.size());
            movieRepository.saveAll(movieList.subList(i, end));
        }
        
        return movies;
    }
    
    private void loadRatingsFromCSV(Map<Long, Movie> movies) throws Exception {
        Map<Long, User> users = new HashMap<>();
        List<Rating> ratings = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("data/ratings.csv").getInputStream()))) {
            
            String line;
            reader.readLine(); // Skip header
            int count = 0;
            
            while ((line = reader.readLine()) != null && count < 10000) { // Limit for demo
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    Long userId = Long.parseLong(parts[0]);
                    Long movieId = Long.parseLong(parts[1]);
                    Double rating = Double.parseDouble(parts[2]);
                    
                    // Get or create user
                    User user = users.computeIfAbsent(userId, id -> {
                        User newUser = new User("user_" + id);
                        return newUser;
                    });
                    
                    // Get movie
                    Movie movie = movies.get(movieId);
                    if (movie != null) {
                        ratings.add(new Rating(user, movie, rating));
                        count++;
                    }
                }
            }
        }
        
        // Save users first
        userRepository.saveAll(users.values());
        
        // Save ratings in batches
        for (int i = 0; i < ratings.size(); i += 1000) {
            int end = Math.min(i + 1000, ratings.size());
            ratingRepository.saveAll(ratings.subList(i, end));
        }
    }
    
    private LocalDate extractYearFromTitle(String title) {
        try {
            if (title.contains("(") && title.contains(")")) {
                String yearStr = title.substring(title.lastIndexOf("(") + 1, title.lastIndexOf(")"));
                int year = Integer.parseInt(yearStr);
                return LocalDate.of(year, 1, 1);
            }
        } catch (Exception e) {
            // Ignore parsing errors
        }
        return LocalDate.of(2000, 1, 1); // Default year
    }
    
    private void loadSampleData() {
        System.out.println("Loading sample data as fallback...");
        // Keep existing sample data as fallback
        List<Movie> sampleMovies = Arrays.asList(
            new Movie("The Shawshank Redemption", "Classic drama", List.of("Drama"), LocalDate.of(1994, 9, 23), 9.3),
            new Movie("The Godfather", "Crime masterpiece", List.of("Crime", "Drama"), LocalDate.of(1972, 3, 24), 9.2),
            new Movie("The Dark Knight", "Superhero thriller", List.of("Action", "Crime"), LocalDate.of(2008, 7, 18), 9.0)
        );
        movieRepository.saveAll(sampleMovies);
        
        // Create sample users and ratings
        User user1 = new User("sample_user_1");
        userRepository.save(user1);
        
        sampleMovies.forEach(movie -> {
            Rating rating = new Rating(user1, movie, 4.5);
            ratingRepository.save(rating);
        });
    }
}