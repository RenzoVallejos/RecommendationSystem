package com.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class UserRatingEvent {
    
    @JsonProperty("userId")
    private Long userId;
    
    @JsonProperty("movieId")
    private Long movieId;
    
    @JsonProperty("rating")
    private Double rating;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    
    @JsonProperty("eventType")
    private String eventType; // "RATING", "VIEW", "LIKE"
    
    public UserRatingEvent() {}
    
    public UserRatingEvent(Long userId, Long movieId, Double rating, String eventType) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }
    
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
}