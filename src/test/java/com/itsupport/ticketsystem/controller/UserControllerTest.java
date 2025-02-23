package com.itsupport.ticketsystem.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.itsupport.ticketsystem.model.User;
import com.itsupport.ticketsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User(1L, "testUser", "password", User.Role.EMPLOYEE);
    }

    @Test
    void testGetUserByUsername() {
        when(userService.getUserByUsername("testUser")).thenReturn(Optional.of(mockUser));
        ResponseEntity<User> response = userController.getUserByUsername("testUser");
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("testUser", response.getBody().getUsername());
    }
}
