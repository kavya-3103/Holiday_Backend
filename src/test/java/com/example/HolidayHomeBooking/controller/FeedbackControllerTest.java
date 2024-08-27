package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.Feedback;
import com.example.HolidayHomeBooking.entity.Place;
import com.example.HolidayHomeBooking.entity.User;
import com.example.HolidayHomeBooking.service.FeedbackService;
import com.example.HolidayHomeBooking.service.PlaceService;
import com.example.HolidayHomeBooking.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @Mock
    private UserService userService;

    @Mock
    private PlaceService placeService;

    @InjectMocks
    private FeedbackController feedbackController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFeedbacks() {
        Feedback feedback1 = new Feedback();
        feedback1.setRating(5);
        feedback1.setComment("Great place!");

        Feedback feedback2 = new Feedback();
        feedback2.setRating(4);
        feedback2.setComment("Good experience!");

        List<Feedback> feedbacks = Arrays.asList(feedback1, feedback2);
        when(feedbackService.getAllFeedbacks()).thenReturn(feedbacks);

        List<Feedback> result = feedbackController.getAllFeedbacks();

        assertEquals(2, result.size());
        assertEquals("Great place!", result.get(0).getComment());
        verify(feedbackService).getAllFeedbacks();
    }

    @Test
    void testGetFeedbackById_FeedbackExists() {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1L);
        feedback.setRating(5);
        feedback.setComment("Great place!");

        when(feedbackService.getFeedbackById(1L)).thenReturn(Optional.of(feedback));

        ResponseEntity<Feedback> response = feedbackController.getFeedbackById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(feedback, response.getBody());
        verify(feedbackService).getFeedbackById(1L);
    }

    @Test
    void testGetFeedbackById_FeedbackDoesNotExist() {
        when(feedbackService.getFeedbackById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            feedbackController.getFeedbackById(1L);
        });
    }

    @Test
    void testCreateFeedback_ValidFeedback() {
        User user = new User();
        user.setUserId(1L);

        Place place = new Place();
        place.setPlaceId(1L);

        Feedback feedback = new Feedback();
        feedback.setRating(5);
        feedback.setComment("Great place!");
        feedback.setUser(user);
        feedback.setPlace(place);

        when(userService.findById(1L)).thenReturn(user);
        when(placeService.findById(1L)).thenReturn(place);
        when(feedbackService.saveFeedback(feedback)).thenReturn(feedback);

        ResponseEntity<Feedback> response = feedbackController.createFeedback(feedback);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(feedback, response.getBody());
        verify(userService).findById(1L);
        verify(placeService).findById(1L);
        verify(feedbackService).saveFeedback(feedback);
    }

    @Test
    void testCreateFeedback_InvalidUserOrPlace() {
        Feedback feedback = new Feedback();
        feedback.setRating(5);
        feedback.setComment("Great place!");
        feedback.setUser(new User()); // User without ID
        feedback.setPlace(new Place()); // Place without ID

        when(userService.findById(anyLong())).thenReturn(null);
        when(placeService.findById(anyLong())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            feedbackController.createFeedback(feedback);
        });
    }

    @Test
    void testUpdateFeedback_FeedbackExists() {
        User user = new User();
        user.setUserId(1L);

        Place place = new Place();
        place.setPlaceId(1L);

        Feedback existingFeedback = new Feedback();
        existingFeedback.setFeedbackId(1L);
        existingFeedback.setRating(5);
        existingFeedback.setComment("Great place!");

        Feedback updatedFeedback = new Feedback();
        updatedFeedback.setRating(4);
        updatedFeedback.setComment("Good experience!");
        updatedFeedback.setUser(user);
        updatedFeedback.setPlace(place);

        when(feedbackService.getFeedbackById(1L)).thenReturn(Optional.of(existingFeedback));
        when(userService.findById(1L)).thenReturn(user);
        when(placeService.findById(1L)).thenReturn(place);
        when(feedbackService.saveFeedback(updatedFeedback)).thenReturn(updatedFeedback);

        ResponseEntity<Feedback> response = feedbackController.updateFeedback(1L, updatedFeedback);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedFeedback, response.getBody());
        verify(feedbackService).getFeedbackById(1L);
        verify(userService).findById(1L);
        verify(placeService).findById(1L);
        verify(feedbackService).saveFeedback(updatedFeedback);
    }

    @Test
    void testUpdateFeedback_FeedbackDoesNotExist() {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1L);

        when(feedbackService.getFeedbackById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            feedbackController.updateFeedback(1L, feedback);
        });
    }

    @Test
    void testDeleteFeedback_FeedbackExists() {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1L);

        when(feedbackService.getFeedbackById(1L)).thenReturn(Optional.of(feedback));

        ResponseEntity<String> response = feedbackController.deleteFeedback(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Feedback deleted successfully", response.getBody());
        verify(feedbackService).getFeedbackById(1L);
        verify(feedbackService).deleteFeedback(1L);
    }

    @Test
    void testDeleteFeedback_FeedbackDoesNotExist() {
        when(feedbackService.getFeedbackById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            feedbackController.deleteFeedback(1L);
        });
    }
}
