package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.Place;
import com.example.HolidayHomeBooking.service.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(PlaceController.class);

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public List<Place> getAllPlaces() {
        logger.trace("TRACE: Entering getAllPlaces method");
        logger.debug("DEBUG: Fetching all places from database");
        logger.info("INFO: Received request to get all places");

        List<Place> places = placeService.getAllPlaces();

        logger.trace("TRACE: Exiting getAllPlaces method with {} places", places.size());
        return places;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable("id") Long placeId) {
        logger.trace("TRACE: Entering getPlaceById method with ID: {}", placeId);
        logger.debug("DEBUG: Fetching place with ID: {}", placeId);
        logger.info("INFO: Received request to get place by ID: {}", placeId);

        Place place = placeService.findById(placeId);
        if (place == null) {
            logger.error("ERROR: Place not found with ID: {}", placeId);
            logger.trace("TRACE: Exiting getPlaceById method with error");
            throw new NoSuchElementException("Place not found with ID: " + placeId);
        }

        logger.trace("TRACE: Exiting getPlaceById method");
        return ResponseEntity.ok(place);
    }

    @PostMapping
    public ResponseEntity<String> createPlace(@RequestBody Place place) {
        logger.trace("TRACE: Entering createPlace method");
        logger.debug("DEBUG: Creating place with details: {}", place);
        logger.info("INFO: Received request to create place");

        // Manual validation
        if (place == null) {
            logger.warn("WARN: Place object is null");
            return ResponseEntity.badRequest().body("Place cannot be null");
        }
        if (place.getName() == null || place.getName().trim().isEmpty()) {
            logger.warn("WARN: Name cannot be null or empty");
            return ResponseEntity.badRequest().body("Name cannot be null or empty");
        }
        if (place.getLocation() == null || place.getLocation().trim().isEmpty()) {
            logger.warn("WARN: Location cannot be null or empty");
            return ResponseEntity.badRequest().body("Location cannot be null or empty");
        }
        if (place.getDescription() == null || place.getDescription().trim().isEmpty()) {
            logger.warn("WARN: Description cannot be null or empty");
            return ResponseEntity.badRequest().body("Description cannot be null or empty");
        }
        if (place.getTourist_Attractions() == null || place.getTourist_Attractions().trim().isEmpty()) {
            logger.warn("WARN: Tourist Attractions cannot be null or empty");
            return ResponseEntity.badRequest().body("Tourist Attractions cannot be null or empty");
        }

        Place createdPlace = placeService.savePlace(place);
        logger.info("INFO: Place created successfully: {}", createdPlace);

        logger.trace("TRACE: Exiting createPlace method");
        return ResponseEntity.status(HttpStatus.CREATED).body("Place created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePlace(@PathVariable("id") Long placeId, @RequestBody Place place) {
        logger.trace("TRACE: Entering updatePlace method with ID: {}", placeId);
        logger.debug("DEBUG: Updating place with ID: {}", placeId);
        logger.info("INFO: Received request to update place with ID: {}", placeId);

        Place existingPlace = placeService.findById(placeId);
        if (existingPlace == null) {
            logger.error("ERROR: Place not found with ID: {}", placeId);
            logger.trace("TRACE: Exiting updatePlace method with error");
            throw new NoSuchElementException("Place not found with ID: " + placeId);
        }

        // Manual validation
        if (place == null) {
            logger.warn("WARN: Place object is null");
            return ResponseEntity.badRequest().body("Place cannot be null");
        }
        if (place.getName() == null || place.getName().trim().isEmpty()) {
            logger.warn("WARN: Name cannot be null or empty");
            return ResponseEntity.badRequest().body("Name cannot be null or empty");
        }
        if (place.getLocation() == null || place.getLocation().trim().isEmpty()) {
            logger.warn("WARN: Location cannot be null or empty");
            return ResponseEntity.badRequest().body("Location cannot be null or empty");
        }
        if (place.getDescription() == null || place.getDescription().trim().isEmpty()) {
            logger.warn("WARN: Description cannot be null or empty");
            return ResponseEntity.badRequest().body("Description cannot be null or empty");
        }
        if (place.getTourist_Attractions() == null || place.getTourist_Attractions().trim().isEmpty()) {
            logger.warn("WARN: Tourist Attractions cannot be null or empty");
            return ResponseEntity.badRequest().body("Tourist Attractions cannot be null or empty");
        }

        // Update the fields of the existingPlace
        existingPlace.setName(place.getName());
        existingPlace.setLocation(place.getLocation());
        existingPlace.setDescription(place.getDescription());
        existingPlace.setTourist_Attractions(place.getTourist_Attractions());

        Place updatedPlace = placeService.savePlace(existingPlace);
        logger.info("INFO: Place updated successfully: {}", updatedPlace);

        logger.trace("TRACE: Exiting updatePlace method");
        return ResponseEntity.ok("Place updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlace(@PathVariable("id") Long placeId) {
        logger.trace("TRACE: Entering deletePlace method with ID: {}", placeId);
        logger.debug("DEBUG: Deleting place with ID: {}", placeId);
        logger.info("INFO: Received request to delete place with ID: {}", placeId);

        Place existingPlace = placeService.findById(placeId);
        if (existingPlace == null) {
            logger.error("ERROR: Place not found with ID: {}", placeId);
            logger.trace("TRACE: Exiting deletePlace method with error");
            throw new NoSuchElementException("Place not found with ID: " + placeId);
        }

        placeService.deletePlace(placeId);
        logger.info("INFO: Place deleted successfully with ID: {}", placeId);

        logger.trace("TRACE: Exiting deletePlace method");
        return ResponseEntity.ok("Place deleted successfully");
    }
}
