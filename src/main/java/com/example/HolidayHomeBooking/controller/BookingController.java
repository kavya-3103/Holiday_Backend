package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.Booking;
import com.example.HolidayHomeBooking.entity.Place;
import com.example.HolidayHomeBooking.entity.User;
import com.example.HolidayHomeBooking.service.BookingService;
import com.example.HolidayHomeBooking.service.PlaceService;
import com.example.HolidayHomeBooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final PlaceService placeService;

    @Autowired
    public BookingController(BookingService bookingService, UserService userService, PlaceService placeService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.placeService = placeService;
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable("id") Long bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            throw new NoSuchElementException("Booking not found with ID: " + bookingId);
        }
        return ResponseEntity.ok(booking);
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking bookingRequest) {
        User user = userService.findById(bookingRequest.getUser().getUserId());
        Place place = placeService.findById(bookingRequest.getPlace().getPlaceId());

        if (user == null || place == null) {
            throw new IllegalArgumentException("User or Place not found");
        }

        bookingRequest.setUser(user);
        bookingRequest.setPlace(place);

        Booking createdBooking = bookingService.saveBooking(bookingRequest);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable("id") Long bookingId, @RequestBody Booking booking) {
        Booking existingBooking = bookingService.getBookingById(bookingId);
        if (existingBooking == null) {
            throw new NoSuchElementException("Booking not found with ID: " + bookingId);
        }

        booking.setBookingId(bookingId);

        if (booking.getUser() == null || booking.getUser().getUserId() == null ||
            booking.getPlace() == null || booking.getPlace().getPlaceId() == null) {
            throw new IllegalArgumentException("Invalid User or Place ID");
        }

        User user = userService.findById(booking.getUser().getUserId());
        Place place = placeService.findById(booking.getPlace().getPlaceId());

        if (user == null || place == null) {
            throw new IllegalArgumentException("User or Place not found");
        }

        booking.setUser(user);
        booking.setPlace(place);

        Booking updatedBooking = bookingService.saveBooking(booking);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable("id") Long bookingId) {
        Booking existingBooking = bookingService.getBookingById(bookingId);
        if (existingBooking == null) {
            throw new NoSuchElementException("Booking not found with ID: " + bookingId);
        }
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.ok("Booking deleted successfully");
    }
}
