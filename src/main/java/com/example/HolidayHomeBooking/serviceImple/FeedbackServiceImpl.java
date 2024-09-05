package com.example.HolidayHomeBooking.serviceImple;

import com.example.HolidayHomeBooking.entity.Feedback;
import com.example.HolidayHomeBooking.repository.IFeedbackRepository;
import com.example.HolidayHomeBooking.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final IFeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackServiceImpl(IFeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    
    @Override
    public Optional<Feedback> getFeedbackById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId);
    }

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public void deleteFeedback(Long feedbackId) {
        feedbackRepository.deleteById(feedbackId);
    }
}