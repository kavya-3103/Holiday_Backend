

package com.example.HolidayHomeBooking.repository;

import com.example.HolidayHomeBooking.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);  // Changed from findByEmail to findByUsername

}
