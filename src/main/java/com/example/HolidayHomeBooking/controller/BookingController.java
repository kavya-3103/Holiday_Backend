package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.Booking;
import com.example.HolidayHomeBooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable("id") Long bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        return booking != null ? ResponseEntity.ok(booking) : ResponseEntity.notFound().build();
    }

//    @PostMapping
//    public Booking createBooking(@RequestBody Booking booking) {
//        return bookingService.saveBooking(booking);
//    }
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = bookingService.saveBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking); // Return 201 Created
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateBooking(@PathVariable("id") Long bookingId, @RequestBody Booking booking) {
        Booking existingBooking = bookingService.getBookingById(bookingId);
        if (existingBooking != null) {
            booking.setBookingId(bookingId);

            // Validate if User and Place are found
            if (booking.getUser() == null || booking.getUser().getUserId() == null) {
                return ResponseEntity.badRequest().body("User ID is missing or invalid");
            }

            if (booking.getPlace() == null || booking.getPlace().getPlaceId() == null) {
                return ResponseEntity.badRequest().body("Place ID is missing or invalid");
            }

            // Save the updated booking
            bookingService.saveBooking(booking);
            return ResponseEntity.ok("Booking updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable("id") Long bookingId) {
        Booking existingBooking = bookingService.getBookingById(bookingId);
        if (existingBooking != null) {
            bookingService.deleteBooking(bookingId);
            return ResponseEntity.ok("Booking deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
