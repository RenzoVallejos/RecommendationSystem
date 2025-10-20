# Movie Recommendation System

A scalable recommendation API built with Java Spring Boot, featuring collaborative filtering, content-based recommendations, and real-time Kafka streaming.

## Features

- **Search API**: Text-based movie search with caching
- **Collaborative Filtering**: User-based recommendations using rating patterns
- **Content-Based Filtering**: Genre-based movie similarity
- **Real-time Streaming**: Kafka integration for live data processing
- **Async Processing**: Non-blocking I/O for high concurrency
- **Caching**: Redis integration for optimized performance
- **REST API**: Clean endpoints with comprehensive documentation
- **MovieLens Dataset**: Real movie data with 50+ movies and ratings

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2
- **Database**: H2 (development), PostgreSQL (production ready)
- **Streaming**: Apache Kafka + Zookeeper
- **Caching**: Redis
- **Documentation**: Swagger/OpenAPI 3.0
- **Build Tool**: Maven
- **Architecture**: Layered (Controller → Service → Repository → Database)

## API Endpoints

### Search
- `GET /api/search?query=matrix` - Search movies by title/description
- `GET /api/search/async?query=matrix` - Async search
- `GET /api/search/genre/Action` - Filter by genre

### Recommendations
- `GET /api/recommend/user/{userId}` - Personalized recommendations
- `GET /api/recommend/user/{userId}/async` - Async recommendations
- `GET /api/recommend/similar/{movieId}` - Similar movies

## API Documentation

**Interactive Swagger UI**: http://localhost:8080/swagger-ui.html

**OpenAPI JSON**: http://localhost:8080/api-docs

## Quick Start

### Prerequisites
```bash
# Install dependencies
brew install kafka zookeeper

# Start services
brew services start zookeeper
brew services start kafka
```

### Run Application
```bash
# Clone the repository
git clone https://github.com/RenzoVallejos/RecommendationSystem.git
cd RecommendationSystem

# Run the application
mvn spring-boot:run

# Test the API
curl "http://localhost:8080/api/search?query=matrix"
curl "http://localhost:8080/api/recommend/user/1"
```

## Architecture

```
HTTP Request → Controller → Service → Repository → Database
                    ↓              ↓
                 Cache          Kafka Streaming
                (Redis)        (Real-time Events)
```

## Real-time Features

- **Live Rating Events**: Users rating movies in real-time
- **View Tracking**: Movie viewing events processed via Kafka
- **Dynamic Recommendations**: Recommendations update as new ratings arrive
- **Event Processing**: Kafka consumers update database in real-time

## Sample Data

The application includes MovieLens dataset for testing:
- 50 popular movies with real metadata
- 10 sample users with rating history
- Genre classifications for content-based filtering
- Real-time event simulation for live data updates

## Configuration

Database and services configuration in `src/main/resources/application.yml`:
- H2 in-memory database for development
- Kafka streaming configuration
- Redis caching settings
- JPA/Hibernate configuration

## Production-Ready Features

- **System Design**: Proper layered architecture
- **Real-time Processing**: Kafka event streaming
- **Scalability**: Async processing and caching
- **Algorithms**: Multiple recommendation strategies
- **Performance**: Optimized queries and response times
- **Documentation**: Complete API documentation with Swagger
- **Maintainability**: Clean code with dependency injection