package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.Place;
import com.example.HolidayHomeBooking.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/places")
@Validated
public class PlaceController {

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public List<Place> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable("id") Long placeId) {
        Place place = placeService.findById(placeId);
        if (place == null) {
            throw new NoSuchElementException("Place not found with ID: " + placeId);
        }
        return ResponseEntity.ok(place);
    }

    @PostMapping
    public ResponseEntity<String> createPlace(@RequestBody Place place) {
        // Manual validation
        if (place == null) {
            return ResponseEntity.badRequest().body("Place cannot be null");
        }
        if (place.getName() == null || place.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name cannot be null or empty");
        }
        if (place.getLocation() == null || place.getLocation().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Location cannot be null or empty");
        }
        if (place.getDescription() == null || place.getDescription().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Description cannot be null or empty");
        }
        if (place.getTourist_Attractions() == null || place.getTourist_Attractions().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tourist Attractions cannot be null or empty");
        }

        Place createdPlace = placeService.savePlace(place);
        return ResponseEntity.status(HttpStatus.CREATED).body("Place created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePlace(@PathVariable("id") Long placeId, @RequestBody Place place) {
        Place existingPlace = placeService.findById(placeId);
        if (existingPlace == null) {
            throw new NoSuchElementException("Place not found with ID: " + placeId);
        }

        // Manual validation
        if (place == null) {
            return ResponseEntity.badRequest().body("Place cannot be null");
        }
        if (place.getName() == null || place.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name cannot be null or empty");
        }
        if (place.getLocation() == null || place.getLocation().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Location cannot be null or empty");
        }
        if (place.getDescription() == null || place.getDescription().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Description cannot be null or empty");
        }
        if (place.getTourist_Attractions() == null || place.getTourist_Attractions().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tourist Attractions cannot be null or empty");
        }

        // Update the fields of the existingPlace
        existingPlace.setName(place.getName());
        existingPlace.setLocation(place.getLocation());
        existingPlace.setDescription(place.getDescription());
        existingPlace.setTourist_Attractions(place.getTourist_Attractions());

        Place updatedPlace = placeService.savePlace(existingPlace);
        return ResponseEntity.ok("Place updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlace(@PathVariable("id") Long placeId) {
        Place existingPlace = placeService.findById(placeId);
        if (existingPlace == null) {
            throw new NoSuchElementException("Place not found with ID: " + placeId);
        }
        placeService.deletePlace(placeId);
        return ResponseEntity.ok("Place deleted successfully");
    }
}
