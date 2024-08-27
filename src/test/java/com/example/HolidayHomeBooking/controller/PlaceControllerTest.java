package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.Place;
import com.example.HolidayHomeBooking.service.PlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlaceControllerTest {

    @Mock
    private PlaceService placeService;

    @InjectMocks
    private PlaceController placeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPlaces() {
        Place place1 = new Place();
        place1.setName("Place1");
        place1.setLocation("Location1");
        place1.setDescription("Description1");
        place1.setTourist_Attractions("Attractions1");

        Place place2 = new Place();
        place2.setName("Place2");
        place2.setLocation("Location2");
        place2.setDescription("Description2");
        place2.setTourist_Attractions("Attractions2");

        List<Place> places = Arrays.asList(place1, place2);
        when(placeService.getAllPlaces()).thenReturn(places);

        List<Place> result = placeController.getAllPlaces();

        assertEquals(2, result.size());
        assertEquals("Place1", result.get(0).getName());
        verify(placeService).getAllPlaces();
    }

    @Test
    void testGetPlaceById_PlaceExists() {
        Place place = new Place();
        place.setPlaceId(1L);
        place.setName("Place1");
        place.setLocation("Location1");
        place.setDescription("Description1");
        place.setTourist_Attractions("Attractions1");

        when(placeService.findById(1L)).thenReturn(place);

        ResponseEntity<Place> response = placeController.getPlaceById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(place, response.getBody());
        verify(placeService).findById(1L);
    }

    @Test
    void testGetPlaceById_PlaceDoesNotExist() {
        when(placeService.findById(1L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> {
            placeController.getPlaceById(1L);
        });
    }

    @Test
    void testCreatePlace_ValidPlace() {
        Place place = new Place();
        place.setName("Place1");
        place.setLocation("Location1");
        place.setDescription("Description1");
        place.setTourist_Attractions("Attractions1");

        when(placeService.savePlace(place)).thenReturn(place);

        ResponseEntity<String> response = placeController.createPlace(place);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Place created successfully", response.getBody());
        verify(placeService).savePlace(place);
    }

    @Test
    void testCreatePlace_InvalidPlace() {
        Place place = new Place(); // All fields are null

        ResponseEntity<String> response = placeController.createPlace(place);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Name cannot be null or empty"));
    }

    @Test
    void testUpdatePlace_PlaceExists() {
        Place existingPlace = new Place();
        existingPlace.setPlaceId(1L);
        existingPlace.setName("Place1");
        existingPlace.setLocation("Location1");
        existingPlace.setDescription("Description1");
        existingPlace.setTourist_Attractions("Attractions1");

        Place updatedPlace = new Place();
        updatedPlace.setName("UpdatedPlace");
        updatedPlace.setLocation("UpdatedLocation");
        updatedPlace.setDescription("UpdatedDescription");
        updatedPlace.setTourist_Attractions("UpdatedAttractions");

        when(placeService.findById(1L)).thenReturn(existingPlace);
        when(placeService.savePlace(existingPlace)).thenReturn(updatedPlace);

        ResponseEntity<String> response = placeController.updatePlace(1L, updatedPlace);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Place updated successfully", response.getBody());
        verify(placeService).findById(1L);
        verify(placeService).savePlace(existingPlace);
    }

    @Test
    void testUpdatePlace_PlaceDoesNotExist() {
        Place updatedPlace = new Place();
        updatedPlace.setName("UpdatedPlace");

        when(placeService.findById(1L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> {
            placeController.updatePlace(1L, updatedPlace);
        });
    }

    @Test
    void testDeletePlace_PlaceExists() {
        Place place = new Place();
        place.setPlaceId(1L);

        when(placeService.findById(1L)).thenReturn(place);

        ResponseEntity<String> response = placeController.deletePlace(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Place deleted successfully", response.getBody());
        verify(placeService).findById(1L);
        verify(placeService).deletePlace(1L);
    }

    @Test
    void testDeletePlace_PlaceDoesNotExist() {
        when(placeService.findById(1L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> {
            placeController.deletePlace(1L);
        });
    }
}
