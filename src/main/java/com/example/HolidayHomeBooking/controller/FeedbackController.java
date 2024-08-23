package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.Feedback;
import com.example.HolidayHomeBooking.entity.Place;
import com.example.HolidayHomeBooking.entity.User;
import com.example.HolidayHomeBooking.service.FeedbackService;
import com.example.HolidayHomeBooking.service.PlaceService;
import com.example.HolidayHomeBooking.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;
    private final PlaceService placeService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService, UserService userService, PlaceService placeService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
        this.placeService = placeService;
    }

    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable("id") Long feedbackId) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(feedbackId);
        return feedback.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    

    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedbackRequest) {
        // Find User and Place entities by their IDs
        User user = userService.findById(feedbackRequest.getUser().getUserId());
        Place place = placeService.findById(feedbackRequest.getPlace().getPlaceId());

        // Validate if User and Place are found
        if (user == null || place == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Set User and Place for the Feedback entity
        feedbackRequest.setUser(user);
        feedbackRequest.setPlace(place);

        // Save Feedback entity
        Feedback createdFeedback = feedbackService.saveFeedback(feedbackRequest);
        return new ResponseEntity<>(createdFeedback, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable("id") Long feedbackId, @RequestBody Feedback feedback) {
        Optional<Feedback> existingFeedback = feedbackService.getFeedbackById(feedbackId);
        if (existingFeedback.isPresent()) {
            feedback.setFeedbackId(feedbackId);

            // Validate User
            if (feedback.getUser() == null || feedback.getUser().getUserId() == null) {
                return ResponseEntity.badRequest().body(null);
            }

            User user = userService.findById(feedback.getUser().getUserId());
            if (user == null) {
                return ResponseEntity.badRequest().body(null);
            }

            // Validate Place
            if (feedback.getPlace() == null || feedback.getPlace().getPlaceId() == null) {
                return ResponseEntity.badRequest().body(null);
            }

            Place place = placeService.findById(feedback.getPlace().getPlaceId());
            if (place == null) {
                return ResponseEntity.badRequest().body(null);
            }

            // Set validated User and Place
            feedback.setUser(user);
            feedback.setPlace(place);

            // Save and return updated Feedback
            Feedback updatedFeedback = feedbackService.saveFeedback(feedback);
            return ResponseEntity.ok(updatedFeedback);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable("id") Long feedbackId) {
        Feedback existingFeedback = feedbackService.getFeedbackById(feedbackId).orElse(null);
        if (existingFeedback != null) {
            feedbackService.deleteFeedback(feedbackId);
            return ResponseEntity.ok("Feedback deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
