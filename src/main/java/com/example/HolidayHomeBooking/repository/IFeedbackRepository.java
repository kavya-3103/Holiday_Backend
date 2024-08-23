package com.example.HolidayHomeBooking.repository;

import com.example.HolidayHomeBooking.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFeedbackRepository extends JpaRepository<Feedback, Long> {
    // Custom query methods (if needed) can be added here
}
