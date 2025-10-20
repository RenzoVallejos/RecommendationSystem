package com.recommendation.config;

import com.recommendation.entity.User;
import com.recommendation.entity.Rating;
import com.recommendation.entity.Movie;
import com.recommendation.repository.UserRepository;
import com.recommendation.repository.RatingRepository;
import com.recommendation.repository.MovieRepository;
import com.recommendation.service.MovieDataService;
import com.recommendation.service.EventProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private MovieDataService movieDataService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private EventProducerService eventProducerService;
    
    @Override
    public void run(String... args) throws Exception {
        if (movieRepository.count() == 0) {
            System.out.println("Starting MovieLens data import...");
            movieDataService.loadMovieLensData();
        } else {
            System.out.println("Database already contains " + movieRepository.count() + " movies");
        }
        
        // Start real-time Kafka event simulation
        eventProducerService.startRealTimeSimulation();
    }
    

}