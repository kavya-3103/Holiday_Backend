package com.example.HolidayHomeBooking.service;

import com.example.HolidayHomeBooking.entity.Booking;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    List<Booking> getAllBookings();
    //Optional<Booking> getBookingById(Long bookingId);
    Booking getBookingById(Long bookingId); 
    Booking saveBooking(Booking booking);
    void deleteBooking(Long bookingId);
}
