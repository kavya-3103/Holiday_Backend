package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.Place;
import com.example.HolidayHomeBooking.entity.User;
import com.example.HolidayHomeBooking.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

@RequestMapping("/places")
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
        return place != null ? ResponseEntity.ok(place) : ResponseEntity.notFound().build();
    }
    


    @PostMapping
    public Place createPlace(@RequestBody Place place) {
        return placeService.savePlace(place);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable("id") Long placeId, @RequestBody Place place) {
        // Retrieve existing place
        Place existingPlace = placeService.findById(placeId);
        if (existingPlace != null) {
            // Set the ID of the place to be updated
            place.setPlaceId(placeId);
            // Save and return the updated place
            Place updatedPlace = placeService.savePlace(place);
            return ResponseEntity.ok(updatedPlace);
        } else {
            // Return 404 Not Found if place does not exist
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlace(@PathVariable("id") Long placeId) {
        // Retrieve existing place
        Place existingPlace = placeService.findById(placeId);
        if (existingPlace != null) {
            // Delete the place
            placeService.deletePlace(placeId);
            return ResponseEntity.ok("Place deleted successfully");
        } else {
            // Return 404 Not Found if place does not exist
            return ResponseEntity.notFound().build();
        }
    }
}
