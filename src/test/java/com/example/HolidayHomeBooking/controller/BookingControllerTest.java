package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.Booking;
import com.example.HolidayHomeBooking.entity.Place;
import com.example.HolidayHomeBooking.entity.User;
import com.example.HolidayHomeBooking.service.BookingService;
import com.example.HolidayHomeBooking.service.PlaceService;
import com.example.HolidayHomeBooking.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private UserService userService;

    @Mock
    private PlaceService placeService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBookings() {
        Booking booking1 = new Booking();
        booking1.setBookingId(1L);
        booking1.setBookingDate(LocalDate.now());
        
        Booking booking2 = new Booking();
        booking2.setBookingId(2L);
        booking2.setBookingDate(LocalDate.now());

        List<Booking> bookings = Arrays.asList(booking1, booking2);
        when(bookingService.getAllBookings()).thenReturn(bookings);

        List<Booking> result = bookingController.getAllBookings();

        assertEquals(2, result.size());
        verify(bookingService).getAllBookings();
    }

    @Test
    void testGetBookingById_BookingExists() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setBookingDate(LocalDate.now());

        when(bookingService.getBookingById(1L)).thenReturn(booking);

        ResponseEntity<Booking> response = bookingController.getBookingById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(booking, response.getBody());
        verify(bookingService).getBookingById(1L);
    }

    @Test
    void testGetBookingById_BookingDoesNotExist() {
        when(bookingService.getBookingById(1L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> {
            bookingController.getBookingById(1L);
        });
    }

    @Test
    void testCreateBooking_ValidBooking() {
        User user = new User();
        user.setUserId(1L);

        Place place = new Place();
        place.setPlaceId(1L);

        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());
        booking.setUser(user);
        booking.setPlace(place);

        when(userService.findById(1L)).thenReturn(user);
        when(placeService.findById(1L)).thenReturn(place);
        when(bookingService.saveBooking(booking)).thenReturn(booking);

        ResponseEntity<Booking> response = bookingController.createBooking(booking);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(booking, response.getBody());
        verify(userService).findById(1L);
        verify(placeService).findById(1L);
        verify(bookingService).saveBooking(booking);
    }

    @Test
    void testCreateBooking_InvalidUserOrPlace() {
        Booking booking = new Booking();
        booking.setUser(new User()); // User without ID
        booking.setPlace(new Place()); // Place without ID

        when(userService.findById(anyLong())).thenReturn(null);
        when(placeService.findById(anyLong())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            bookingController.createBooking(booking);
        });
    }

    @Test
    void testUpdateBooking_BookingExists() {
        User user = new User();
        user.setUserId(1L);

        Place place = new Place();
        place.setPlaceId(1L);

        Booking existingBooking = new Booking();
        existingBooking.setBookingId(1L);
        existingBooking.setBookingDate(LocalDate.now());

        Booking updatedBooking = new Booking();
        updatedBooking.setBookingId(1L);
        updatedBooking.setBookingDate(LocalDate.now());
        updatedBooking.setUser(user);
        updatedBooking.setPlace(place);

        when(bookingService.getBookingById(1L)).thenReturn(existingBooking);
        when(userService.findById(1L)).thenReturn(user);
        when(placeService.findById(1L)).thenReturn(place);
        when(bookingService.saveBooking(updatedBooking)).thenReturn(updatedBooking);

        ResponseEntity<Booking> response = bookingController.updateBooking(1L, updatedBooking);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBooking, response.getBody());
        verify(bookingService).getBookingById(1L);
        verify(userService).findById(1L);
        verify(placeService).findById(1L);
        verify(bookingService).saveBooking(updatedBooking);
    }

    @Test
    void testUpdateBooking_BookingDoesNotExist() {
        Booking booking = new Booking();
        booking.setBookingId(1L);

        when(bookingService.getBookingById(1L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> {
            bookingController.updateBooking(1L, booking);
        });
    }

    @Test
    void testDeleteBooking_BookingExists() {
        Booking booking = new Booking();
        booking.setBookingId(1L);

        when(bookingService.getBookingById(1L)).thenReturn(booking);

        ResponseEntity<String> response = bookingController.deleteBooking(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Booking deleted successfully", response.getBody());
        verify(bookingService).getBookingById(1L);
        verify(bookingService).deleteBooking(1L);
    }

    @Test
    void testDeleteBooking_BookingDoesNotExist() {
        when(bookingService.getBookingById(1L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> {
            bookingController.deleteBooking(1L);
        });
    }
}
