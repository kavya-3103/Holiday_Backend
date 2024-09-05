package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.User;
import com.example.HolidayHomeBooking.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        logger.trace("TRACE: Entering getAllUsers method");
        logger.debug("DEBUG: Fetching all users from database");
        logger.info("INFO: Received request to get all users");

        List<User> users = userService.getAllUsers();

        logger.trace("TRACE: Exiting getAllUsers method with {} users", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) {
        logger.trace("TRACE: Entering getUserById method with ID: {}", userId);
        logger.debug("DEBUG: Fetching user with ID: {}", userId);
        logger.info("INFO: Received request to get user by ID: {}", userId);

        User user = userService.findById(userId);
        if (user == null) {
            logger.error("ERROR: User not found with ID: {}", userId);
            logger.trace("TRACE: Exiting getUserById method with error");
            throw new NoSuchElementException("User not found with ID: " + userId);
        }

        logger.trace("TRACE: Exiting getUserById method");
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.trace("TRACE: Entering createUser method");
        logger.debug("DEBUG: Creating user with details: {}", user);
        logger.info("INFO: Received request to create user");

        if (user == null) {
            logger.warn("WARN: User object is null");
            return ResponseEntity.badRequest().body(null);  // Note: Returning null body for bad request
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            logger.warn("WARN: Username cannot be null or empty");
            return ResponseEntity.badRequest().body(null);
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            logger.warn("WARN: Email cannot be null or empty");
            return ResponseEntity.badRequest().body(null);
        }

        User createdUser = userService.saveUser(user);
        logger.info("INFO: User created successfully: {}", createdUser);

        logger.trace("TRACE: Exiting createUser method");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        logger.trace("TRACE: Entering loginUser method with username: {}", username);
        logger.debug("DEBUG: Attempting login with username: {}", username);
        logger.info("INFO: Received login request for username: {}", username);

        Optional<User> userOptional = userService.authenticateUser(username, password);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String jsonResponse = String.format("{\"message\": \"Login successful\", \"userId\": %d, \"role\": \"%s\"}", user.getUserId(), user.getRole());
            logger.info("INFO: Login successful for username: {}", username);
            logger.trace("TRACE: Exiting loginUser method");
            return ResponseEntity.ok(jsonResponse);
        } else {
            logger.warn("WARN: Invalid login attempt for username: {}", username);
            logger.trace("TRACE: Exiting loginUser method with failure");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long userId, @RequestBody User userDetails) {
        logger.trace("TRACE: Entering updateUser method with ID: {}", userId);
        logger.debug("DEBUG: Updating user with ID: {}", userId);
        logger.info("INFO: Received request to update user with ID: {}", userId);

        User existingUser = userService.findById(userId);
        if (existingUser == null) {
            logger.error("ERROR: User not found with ID: {}", userId);
            logger.trace("TRACE: Exiting updateUser method with error");
            throw new NoSuchElementException("User not found with ID: " + userId);
        }

        // Update only the fields that are provided in the request
        if (userDetails.getUsername() != null && !userDetails.getUsername().trim().isEmpty()) {
            existingUser.setUsername(userDetails.getUsername());
        }
        if (userDetails.getEmail() != null && !userDetails.getEmail().trim().isEmpty()) {
            existingUser.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPassword() != null && !userDetails.getPassword().trim().isEmpty()) {
            existingUser.setPassword(userDetails.getPassword()); // In a real application, consider hashing the password
        }

        User updatedUser = userService.saveUser(existingUser);
        logger.info("INFO: User updated successfully: {}", updatedUser);

        logger.trace("TRACE: Exiting updateUser method");
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        logger.trace("TRACE: Entering deleteUser method with ID: {}", userId);
        logger.debug("DEBUG: Deleting user with ID: {}", userId);
        logger.info("INFO: Received request to delete user with ID: {}", userId);

        User existingUser = userService.findById(userId);
        if (existingUser == null) {
            logger.error("ERROR: User not found with ID: {}", userId);
            logger.trace("TRACE: Exiting deleteUser method with error");
            throw new NoSuchElementException("User not found with ID: " + userId);
        }

        userService.deleteUser(userId);
        logger.info("INFO: User deleted successfully with ID: {}", userId);

        logger.trace("TRACE: Exiting deleteUser method");
        return ResponseEntity.ok("User deleted successfully");
    }
}
