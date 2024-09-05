package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.Feedback;
import com.example.HolidayHomeBooking.entity.Place;
import com.example.HolidayHomeBooking.entity.User;
import com.example.HolidayHomeBooking.service.FeedbackService;
import com.example.HolidayHomeBooking.service.PlaceService;
import com.example.HolidayHomeBooking.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

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
        logger.trace("TRACE: Entering getAllFeedbacks method");
        logger.debug("DEBUG: Fetching all feedbacks from the database");
        logger.info("INFO: Received request to get all feedbacks");

        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        
        logger.trace("TRACE: Exiting getAllFeedbacks method with {} feedbacks", feedbacks.size());
        return feedbacks;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable("id") Long feedbackId) {
        logger.trace("TRACE: Entering getFeedbackById method with ID: {}", feedbackId);
        logger.debug("DEBUG: Fetching feedback with ID: {}", feedbackId);
        logger.info("INFO: Received request to get feedback by ID: {}", feedbackId);

        Optional<Feedback> feedback = feedbackService.getFeedbackById(feedbackId);
        if (feedback.isEmpty()) {
            logger.error("ERROR: Feedback not found with ID: {}", feedbackId);
            logger.trace("TRACE: Exiting getFeedbackById method with error");
            throw new NoSuchElementException("Feedback not found with ID: " + feedbackId);
        }

        logger.info("INFO: Retrieved feedback with ID: {}", feedbackId);
        logger.trace("TRACE: Exiting getFeedbackById method");
        return ResponseEntity.ok(feedback.get());
    }

    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedbackRequest) {
        logger.trace("TRACE: Entering createFeedback method");
        logger.debug("DEBUG: Creating feedback for user ID: {} and place ID: {}", feedbackRequest.getUser().getUserId(), feedbackRequest.getPlace().getPlaceId());
        logger.info("INFO: Received request to create feedback");

        User user = userService.findById(feedbackRequest.getUser().getUserId());
        Place place = placeService.findById(feedbackRequest.getPlace().getPlaceId());

        if (user == null || place == null) {
            logger.error("ERROR: User or Place not found for feedback creation");
            logger.trace("TRACE: Exiting createFeedback method with error");
            throw new IllegalArgumentException("User or Place not found");
        }

        feedbackRequest.setUser(user);
        feedbackRequest.setPlace(place);

        Feedback createdFeedback = feedbackService.saveFeedback(feedbackRequest);
        logger.info("INFO: Feedback created with ID: {}", createdFeedback.getFeedbackId());

        logger.trace("TRACE: Exiting createFeedback method");
        return new ResponseEntity<>(createdFeedback, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable("id") Long feedbackId, @RequestBody Feedback feedback) {
        logger.trace("TRACE: Entering updateFeedback method with ID: {}", feedbackId);
        logger.debug("DEBUG: Updating feedback with ID: {}", feedbackId);
        logger.info("INFO: Received request to update feedback with ID: {}", feedbackId);

        Optional<Feedback> existingFeedback = feedbackService.getFeedbackById(feedbackId);
        if (existingFeedback.isEmpty()) {
            logger.error("ERROR: Feedback not found with ID: {}", feedbackId);
            logger.trace("TRACE: Exiting updateFeedback method with error");
            throw new NoSuchElementException("Feedback not found with ID: " + feedbackId);
        }

        feedback.setFeedbackId(feedbackId);

        if (feedback.getUser() == null || feedback.getUser().getUserId() == null ||
            feedback.getPlace() == null || feedback.getPlace().getPlaceId() == null) {
            logger.error("ERROR: Invalid User or Place ID for feedback update");
            logger.trace("TRACE: Exiting updateFeedback method with error");
            throw new IllegalArgumentException("Invalid User or Place ID");
        }

        User user = userService.findById(feedback.getUser().getUserId());
        Place place = placeService.findById(feedback.getPlace().getPlaceId());

        if (user == null || place == null) {
            logger.error("ERROR: User or Place not found for feedback update");
            logger.trace("TRACE: Exiting updateFeedback method with error");
            throw new IllegalArgumentException("User or Place not found");
        }

        feedback.setUser(user);
        feedback.setPlace(place);

        Feedback updatedFeedback = feedbackService.saveFeedback(feedback);
        logger.info("INFO: Feedback updated with ID: {}", feedbackId);

        logger.trace("TRACE: Exiting updateFeedback method");
        return ResponseEntity.ok(updatedFeedback);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable("id") Long feedbackId) {
        logger.trace("TRACE: Entering deleteFeedback method with ID: {}", feedbackId);
        logger.debug("DEBUG: Deleting feedback with ID: {}", feedbackId);
        logger.info("INFO: Received request to delete feedback with ID: {}", feedbackId);

        Optional<Feedback> existingFeedback = feedbackService.getFeedbackById(feedbackId);
        if (existingFeedback.isEmpty()) {
            logger.error("ERROR: Feedback not found with ID: {}", feedbackId);
            logger.trace("TRACE: Exiting deleteFeedback method with error");
            throw new NoSuchElementException("Feedback not found with ID: " + feedbackId);
        }

        feedbackService.deleteFeedback(feedbackId);
        logger.info("INFO: Feedback deleted with ID: {}", feedbackId);

        logger.trace("TRACE: Exiting deleteFeedback method");
        return ResponseEntity.ok("Feedback deleted successfully");
    }
}
