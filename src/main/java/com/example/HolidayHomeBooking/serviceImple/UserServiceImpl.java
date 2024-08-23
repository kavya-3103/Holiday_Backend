package com.example.HolidayHomeBooking.serviceImple;

import com.example.HolidayHomeBooking.entity.User;
import com.example.HolidayHomeBooking.repository.IUserRepository;
import com.example.HolidayHomeBooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final IUserRepository userRepository;

    @Autowired
    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
    @Override
    public Optional<User> getUserByUsername(String username) {  // Changed from getUserByEmail to getUserByUsername
        return userRepository.findByUsername(username);  // Changed from findByEmail to findByUsername
    }

    @Override
    public Optional<User> authenticateUser(String username, String password) {  // Changed from email to username
        return userRepository.findByUsername(username)
            .filter(user -> user.getPassword().equals(password)); // In a real-world application, use password hashing
    }

}
