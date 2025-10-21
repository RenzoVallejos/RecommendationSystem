package com.recommendation.controller;

import com.recommendation.entity.User;
import com.recommendation.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User CRUD operations")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users in the database")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create user", description = "Add a new user to the database")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id, 
            @RequestBody User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(userDetails.getUsername());
            return ResponseEntity.ok(userRepository.save(user));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Remove a user from the database")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}