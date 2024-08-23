package com.example.HolidayHomeBooking.service;

import com.example.HolidayHomeBooking.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    User findById(Long id);
    User saveUser(User user);
    void deleteUser(Long userId);
    Optional<User> getUserByUsername(String username); 
    Optional<User> authenticateUser(String username, String password); 
}
