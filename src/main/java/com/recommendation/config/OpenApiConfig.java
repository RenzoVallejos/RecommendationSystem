package com.recommendation.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Movie Recommendation System API",
        version = "1.0.0",
        description = "A scalable recommendation API built with Java Spring Boot, featuring collaborative filtering, content-based recommendations, and real-time Kafka streaming.",
        contact = @Contact(
            name = "Renzo Vallejos",
            url = "https://github.com/RenzoVallejos"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Development Server")
    }
)
public class OpenApiConfig {
}