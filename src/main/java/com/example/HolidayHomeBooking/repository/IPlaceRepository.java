package com.example.HolidayHomeBooking.repository;

import com.example.HolidayHomeBooking.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlaceRepository extends JpaRepository<Place, Long> {
    // Custom query methods (if needed) can be added here
}
