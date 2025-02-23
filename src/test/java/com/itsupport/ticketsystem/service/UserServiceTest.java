package com.itsupport.ticketsystem.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.itsupport.ticketsystem.model.User;
import com.itsupport.ticketsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;  // ✅ Mock PasswordEncoder

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User(1L, "testUser", "password", User.Role.EMPLOYEE);
    }

    @Test
    void testCreateUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");  // ✅ Mock password encoding
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User createdUser = userService.createUser(mockUser);
        assertNotNull(createdUser);
        assertEquals("testUser", createdUser.getUsername());
    }

    @Test
    void testGetUserByUsername() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));

        Optional<User> retrievedUser = userService.getUserByUsername("testUser");
        assertTrue(retrievedUser.isPresent());
        assertEquals("testUser", retrievedUser.get().getUsername());
    }
}
