package com.recommendation.service;

import com.recommendation.dto.UserRatingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class EventProducerService {
    
    private static final String RATING_TOPIC = "user-ratings";
    private static final String VIEW_TOPIC = "movie-views";
    
    @Autowired
    private KafkaTemplate<String, UserRatingEvent> kafkaTemplate;
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final Random random = new Random();
    
    public void startRealTimeSimulation() {
        System.out.println("Starting real-time event simulation...");
        
        // Simulate user ratings every 5 seconds
        scheduler.scheduleAtFixedRate(this::simulateUserRating, 0, 5, TimeUnit.SECONDS);
        
        // Simulate movie views every 2 seconds
        scheduler.scheduleAtFixedRate(this::simulateMovieView, 0, 2, TimeUnit.SECONDS);
    }
    
    private void simulateUserRating() {
        // Generate realistic user rating event
        Long userId = (long) (1 + random.nextInt(100)); // 100 active users
        Long movieId = (long) (1 + random.nextInt(50)); // 50 movies
        Double rating = 1.0 + (random.nextDouble() * 4.0); // 1.0 to 5.0
        rating = Math.round(rating * 2.0) / 2.0; // Round to nearest 0.5
        
        UserRatingEvent event = new UserRatingEvent(userId, movieId, rating, "RATING");
        
        kafkaTemplate.send(RATING_TOPIC, "user-" + userId, event);
        System.out.println("Rating Event: User " + userId + " rated Movie " + movieId + " -> " + rating);
    }
    
    private void simulateMovieView() {
        // Generate movie view event (no rating, just viewing)
        Long userId = (long) (1 + random.nextInt(100));
        Long movieId = (long) (1 + random.nextInt(50));
        
        UserRatingEvent event = new UserRatingEvent(userId, movieId, null, "VIEW");
        
        kafkaTemplate.send(VIEW_TOPIC, "user-" + userId, event);
        System.out.println("View Event: User " + userId + " viewed Movie " + movieId);
    }
    
    public void publishRatingEvent(Long userId, Long movieId, Double rating) {
        UserRatingEvent event = new UserRatingEvent(userId, movieId, rating, "RATING");
        kafkaTemplate.send(RATING_TOPIC, "user-" + userId, event);
    }
    
    public void stopSimulation() {
        scheduler.shutdown();
    }
}