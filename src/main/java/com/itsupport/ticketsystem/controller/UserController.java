package com.itsupport.ticketsystem.controller;

import com.itsupport.ticketsystem.model.Ticket;
import com.itsupport.ticketsystem.model.User;
import com.itsupport.ticketsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        System.out.println("Login Request Received: " + user.getUsername());

        Optional<User> foundUser = userService.getUserByUsername(user.getUsername());

        if (foundUser.isPresent() && passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
            User loggedInUser = foundUser.get();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("id", loggedInUser.getId());
            response.put("username", loggedInUser.getUsername());
            response.put("role", loggedInUser.getRole().toString());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"status\": \"error\"}");
        }
    }

    @GetMapping("/{username}/tickets")
    public ResponseEntity<List<Ticket>> getUserTickets(@PathVariable String username) {
        Optional<User> userOpt = userService.getUserByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();

        if (user.getRole() != User.Role.EMPLOYEE) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Ticket> tickets = userService.getUserTickets(user.getId());
        return ResponseEntity.ok(tickets);
    }

}
