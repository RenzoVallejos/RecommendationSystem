package com.recommendation.repository;

import com.recommendation.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Movie> searchByTitleOrDescription(@Param("query") String query);
    
    List<Movie> findByGenresContaining(String genre);
    
    List<Movie> findByRatingGreaterThanEqual(Double rating);
    
    @Query("SELECT m FROM Movie m WHERE m.embedding IS NOT NULL")
    List<Movie> findAllWithEmbeddings();
}