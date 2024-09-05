package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.Booking;
import com.example.HolidayHomeBooking.entity.Place;
import com.example.HolidayHomeBooking.entity.User;
import com.example.HolidayHomeBooking.service.BookingService;
import com.example.HolidayHomeBooking.service.PlaceService;
import com.example.HolidayHomeBooking.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

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
        logger.trace("TRACE: Entering getAllBookings method");
        logger.debug("DEBUG: Fetching all bookings from the database");
        logger.info("INFO: Received request to get all bookings");

        List<Booking> bookings = bookingService.getAllBookings();
        
        logger.info("INFO: Retrieved {} bookings", bookings.size());
        logger.trace("TRACE: Exiting getAllBookings method");
        return bookings;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable("id") Long bookingId) {
        logger.trace("TRACE: Entering getBookingById method with ID: {}", bookingId);
        logger.debug("DEBUG: Fetching booking with ID: {}", bookingId);
        logger.info("INFO: Received request to get booking by ID: {}", bookingId);

        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            logger.error("ERROR: Booking not found with ID: {}", bookingId);
            logger.trace("TRACE: Exiting getBookingById method with error");
            throw new NoSuchElementException("Booking not found with ID: " + bookingId);
        }

        logger.info("INFO: Retrieved booking with ID: {}", bookingId);
        logger.trace("TRACE: Exiting getBookingById method");
        return ResponseEntity.ok(booking);
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking bookingRequest) {
        logger.trace("TRACE: Entering createBooking method");
        logger.debug("DEBUG: Creating booking for user ID: {} and place ID: {}", bookingRequest.getUser().getUserId(), bookingRequest.getPlace().getPlaceId());
        logger.info("INFO: Received request to create booking");

        User user = userService.findById(bookingRequest.getUser().getUserId());
        Place place = placeService.findById(bookingRequest.getPlace().getPlaceId());

        if (user == null || place == null) {
            logger.error("ERROR: User or Place not found for booking creation");
            logger.trace("TRACE: Exiting createBooking method with error");
            throw new IllegalArgumentException("User or Place not found");
        }

        bookingRequest.setUser(user);
        bookingRequest.setPlace(place);

        Booking createdBooking = bookingService.saveBooking(bookingRequest);
        logger.info("INFO: Booking created with ID: {}", createdBooking.getBookingId());

        logger.trace("TRACE: Exiting createBooking method");
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable("id") Long bookingId, @RequestBody Booking booking) {
        logger.trace("TRACE: Entering updateBooking method with ID: {}", bookingId);
        logger.debug("DEBUG: Updating booking with ID: {}", bookingId);
        logger.info("INFO: Received request to update booking with ID: {}", bookingId);

        Booking existingBooking = bookingService.getBookingById(bookingId);
        if (existingBooking == null) {
            logger.error("ERROR: Booking not found with ID: {}", bookingId);
            logger.trace("TRACE: Exiting updateBooking method with error");
            throw new NoSuchElementException("Booking not found with ID: " + bookingId);
        }

        booking.setBookingId(bookingId);

        if (booking.getUser() == null || booking.getUser().getUserId() == null ||
            booking.getPlace() == null || booking.getPlace().getPlaceId() == null) {
            logger.error("ERROR: Invalid User or Place ID for booking update");
            logger.trace("TRACE: Exiting updateBooking method with error");
            throw new IllegalArgumentException("Invalid User or Place ID");
        }

        User user = userService.findById(booking.getUser().getUserId());
        Place place = placeService.findById(booking.getPlace().getPlaceId());

        if (user == null || place == null) {
            logger.error("ERROR: User or Place not found for booking update");
            logger.trace("TRACE: Exiting updateBooking method with error");
            throw new IllegalArgumentException("User or Place not found");
        }

        booking.setUser(user);
        booking.setPlace(place);

        Booking updatedBooking = bookingService.saveBooking(booking);
        logger.info("INFO: Booking updated with ID: {}", bookingId);

        logger.trace("TRACE: Exiting updateBooking method");
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable("id") Long bookingId) {
        logger.trace("TRACE: Entering deleteBooking method with ID: {}", bookingId);
        logger.debug("DEBUG: Deleting booking with ID: {}", bookingId);
        logger.info("INFO: Received request to delete booking with ID: {}", bookingId);

        Booking existingBooking = bookingService.getBookingById(bookingId);
        if (existingBooking == null) {
            logger.error("ERROR: Booking not found with ID: {}", bookingId);
            logger.trace("TRACE: Exiting deleteBooking method with error");
            throw new NoSuchElementException("Booking not found with ID: " + bookingId);
        }

        bookingService.deleteBooking(bookingId);
        logger.info("INFO: Booking deleted with ID: {}", bookingId);

        logger.trace("TRACE: Exiting deleteBooking method");
        return ResponseEntity.ok("Booking deleted successfully");
    }
}
