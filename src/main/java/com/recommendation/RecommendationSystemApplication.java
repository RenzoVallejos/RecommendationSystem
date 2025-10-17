package com.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RecommendationSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecommendationSystemApplication.class, args);
    }
}