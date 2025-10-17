# Movie Recommendation System

A scalable recommendation API built with Java Spring Boot, featuring collaborative filtering, content-based recommendations, and async processing.

##  Features

- **Search API**: Text-based movie search with caching
- **Collaborative Filtering**: User-based recommendations using rating patterns
- **Content-Based Filtering**: Genre-based movie similarity
- **Async Processing**: Non-blocking I/O for high concurrency
- **Caching**: Redis integration for optimized performance
- **REST API**: Clean endpoints with proper HTTP responses

##  Tech Stack

- **Backend**: Java 17, Spring Boot 3.2
- **Database**: H2 (development), PostgreSQL (production ready)
- **Caching**: Redis
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

## 🏃‍♂️ Quick Start

```bash
# Clone the repository
git clone <your-repo-url>
cd RecommendationSystem

# Run the application
mvn spring-boot:run

# Test the API
curl "http://localhost:8080/api/search?query=matrix"
curl "http://localhost:8080/api/recommend/user/1"
```

##  Architecture

```
HTTP Request → Controller → Service → Repository → Database
                    ↓
                 Cache (Redis)
```

## 📊 Sample Data

The application includes sample movies and user ratings for testing:
- 5 popular movies (Matrix, Inception, Interstellar, Dark Knight, Pulp Fiction)
- 3 test users with rating history
- Genre classifications for content-based filtering

##  Configuration

Database and cache settings in `src/main/resources/application.yml`:
- H2 in-memory database for development
- Redis caching configuration
- JPA/Hibernate settings
