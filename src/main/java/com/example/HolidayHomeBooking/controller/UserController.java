package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.User;
import com.example.HolidayHomeBooking.service.UserService;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new NoSuchElementException("User not found with ID: " + userId);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }


        User createdUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        Optional<User> userOptional = userService.authenticateUser(username, password);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String jsonResponse = String.format("{\"message\": \"Login successful\", \"userId\": %d, \"role\": \"%s\"}", user.getUserId(), user.getRole());
            return ResponseEntity.ok(jsonResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable("id") Long userId, @RequestBody User user) {
//        User existingUser = userService.findById(userId);
//        if (existingUser == null) {
//            throw new NoSuchElementException("User not found with ID: " + userId);
//        }
//        user.setUserId(userId);
//        User updatedUser = userService.saveUser(user);
//        return ResponseEntity.ok(updatedUser);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long userId, @RequestBody User userDetails) {
        User existingUser = userService.findById(userId);
        if (existingUser == null) {
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

        // Do not update the role if it's not provided in the request
        // existingUser.setRole(userDetails.getRole()); // Commented out to preserve existing role

        User updatedUser = userService.saveUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        User existingUser = userService.findById(userId);
        if (existingUser == null) {
            throw new NoSuchElementException("User not found with ID: " + userId);
        }
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}