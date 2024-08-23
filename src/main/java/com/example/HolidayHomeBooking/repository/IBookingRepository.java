package com.example.HolidayHomeBooking.repository;

import com.example.HolidayHomeBooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookingRepository extends JpaRepository<Booking, Long> {
    // Custom query methods (if needed) can be added here
}
