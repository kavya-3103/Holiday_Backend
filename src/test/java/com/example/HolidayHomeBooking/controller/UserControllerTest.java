package com.example.HolidayHomeBooking.controller;

import com.example.HolidayHomeBooking.entity.User;
import com.example.HolidayHomeBooking.service.UserService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setUserId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password");
        user1.setRole("USER");

        User user2 = new User();
        user2.setUserId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password");
        user2.setRole("USER");

        List<User> userList = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(userList);

        List<User> result = userController.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        verify(userService).getAllUsers();
    }

    @Test
    void testGetUserById_UserExists() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setPassword("password");
        user.setRole("USER");

        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).findById(1L);
    }

    @Test
    void testGetUserById_UserDoesNotExist() {
        when(userService.findById(1L)).thenThrow(new NoSuchElementException("User not found with ID: 1"));

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            userController.getUserById(1L);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
    }

    @Test
    void testCreateUser_ValidUser() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setPassword("password");
        user.setRole("USER");

        when(userService.saveUser(user)).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).saveUser(user);
    }

    @Test
    void testCreateUser_NullUser() {
        ResponseEntity<User> response = userController.createUser(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreateUser_EmptyUsername() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername(""); // Empty username
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setRole("USER");

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreateUser_EmptyEmail() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("user1");
        user.setEmail(""); // Empty email
        user.setPassword("password");
        user.setRole("USER");

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testLoginUser_ValidCredentials() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setPassword("password");
        user.setRole("USER");

        when(userService.authenticateUser("user1", "password")).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userController.loginUser("user1", "password");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        String responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.contains("Login successful"));
        assertTrue(responseBody.contains("\"userId\": 1"));
        assertTrue(responseBody.contains("\"role\": \"USER\""));
        verify(userService).authenticateUser("user1", "password");
    }

    @Test
    void testLoginUser_InvalidCredentials() {
        when(userService.authenticateUser("user1", "wrongpassword")).thenReturn(Optional.empty());

        ResponseEntity<String> response = userController.loginUser("user1", "wrongpassword");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    void testUpdateUser_UserExists() {
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("user1");
        existingUser.setEmail("user1@example.com");
        existingUser.setPassword("password");
        existingUser.setRole("USER");

        User updatedUser = new User();
        updatedUser.setUserId(1L);
        updatedUser.setUsername("updatedUser");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newpassword");
        updatedUser.setRole("USER");

        when(userService.findById(1L)).thenReturn(existingUser);
        when(userService.saveUser(existingUser)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.updateUser(1L, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService).findById(1L);
        verify(userService).saveUser(existingUser);
    }

    @Test
    void testUpdateUser_UserDoesNotExist() {
        User updatedUser = new User();
        updatedUser.setUserId(1L);
        updatedUser.setUsername("updatedUser");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newpassword");
        updatedUser.setRole("USER");

        when(userService.findById(1L)).thenThrow(new NoSuchElementException("User not found with ID: 1"));

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            userController.updateUser(1L, updatedUser);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
    }

    @Test
    void testDeleteUser_UserExists() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setPassword("password");
        user.setRole("USER");

        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<String> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
        verify(userService).findById(1L);
        verify(userService).deleteUser(1L);
    }

    @Test
    void testDeleteUser_UserDoesNotExist() {
        when(userService.findById(1L)).thenThrow(new NoSuchElementException("User not found with ID: 1"));

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            userController.deleteUser(1L);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
    }
}
