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
import java.util.NoSuchElementException;

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
        if (feedback.isEmpty()) {
            throw new NoSuchElementException("Feedback not found with ID: " + feedbackId);
        }
        return ResponseEntity.ok(feedback.get());
    }

    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedbackRequest) {
        User user = userService.findById(feedbackRequest.getUser().getUserId());
        Place place = placeService.findById(feedbackRequest.getPlace().getPlaceId());

        if (user == null || place == null) {
            throw new IllegalArgumentException("User or Place not found");
        }

        feedbackRequest.setUser(user);
        feedbackRequest.setPlace(place);

        Feedback createdFeedback = feedbackService.saveFeedback(feedbackRequest);
        return new ResponseEntity<>(createdFeedback, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable("id") Long feedbackId, @RequestBody Feedback feedback) {
        Optional<Feedback> existingFeedback = feedbackService.getFeedbackById(feedbackId);
        if (existingFeedback.isEmpty()) {
            throw new NoSuchElementException("Feedback not found with ID: " + feedbackId);
        }

        feedback.setFeedbackId(feedbackId);

        if (feedback.getUser() == null || feedback.getUser().getUserId() == null ||
            feedback.getPlace() == null || feedback.getPlace().getPlaceId() == null) {
            throw new IllegalArgumentException("Invalid User or Place ID");
        }

        User user = userService.findById(feedback.getUser().getUserId());
        Place place = placeService.findById(feedback.getPlace().getPlaceId());

        if (user == null || place == null) {
            throw new IllegalArgumentException("User or Place not found");
        }

        feedback.setUser(user);
        feedback.setPlace(place);

        Feedback updatedFeedback = feedbackService.saveFeedback(feedback);
        return ResponseEntity.ok(updatedFeedback);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable("id") Long feedbackId) {
        Optional<Feedback> existingFeedback = feedbackService.getFeedbackById(feedbackId);
        if (existingFeedback.isEmpty()) {
            throw new NoSuchElementException("Feedback not found with ID: " + feedbackId);
        }
        feedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.ok("Feedback deleted successfully");
    }
}
