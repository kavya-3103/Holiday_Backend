package com.example.HolidayHomeBooking.service;

import com.example.HolidayHomeBooking.entity.Place;
import java.util.List;
import java.util.Optional;

public interface PlaceService {
    List<Place> getAllPlaces();
    //Optional<Place> getPlaceById(Long placeId);
    Place findById(Long id);
    Place savePlace(Place place);
    void deletePlace(Long placeId);
}
