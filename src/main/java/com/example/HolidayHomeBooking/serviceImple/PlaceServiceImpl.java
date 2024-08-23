package com.example.HolidayHomeBooking.serviceImple;

import com.example.HolidayHomeBooking.entity.Place;
import com.example.HolidayHomeBooking.repository.IPlaceRepository;
import com.example.HolidayHomeBooking.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceServiceImpl implements PlaceService {
    private final IPlaceRepository placeRepository;

    @Autowired
    public PlaceServiceImpl(IPlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

//    @Override
//    public Optional<Place> getPlaceById(Long placeId) {
//        return placeRepository.findById(placeId);
//    }

    @Override
    public Place findById(Long id) {
        return placeRepository.findById(id).orElse(null);
    }
    @Override
    public Place savePlace(Place place) {
        return placeRepository.save(place);
    }

    @Override
    public void deletePlace(Long placeId) {
        placeRepository.deleteById(placeId);
    }

//	@Override
//	public Optional<Place> getPlaceById(Long placeId) {
//		// TODO Auto-generated method stub
//		return Optional.empty();
//	}
}

