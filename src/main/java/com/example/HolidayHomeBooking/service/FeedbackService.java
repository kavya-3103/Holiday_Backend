package com.example.HolidayHomeBooking.service;

import com.example.HolidayHomeBooking.entity.Feedback;
import java.util.List;
import java.util.Optional;

public interface FeedbackService {
    List<Feedback> getAllFeedbacks();
    Optional<Feedback> getFeedbackById(Long feedbackId);
    Feedback saveFeedback(Feedback feedback);
    void deleteFeedback(Long feedbackId);
    
}
