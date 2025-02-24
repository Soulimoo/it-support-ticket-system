package com.itsupport.ticketsystem.controller;

import com.itsupport.ticketsystem.model.Ticket;
import com.itsupport.ticketsystem.model.User;
import com.itsupport.ticketsystem.service.TicketService;
import com.itsupport.ticketsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        System.out.println("Received Ticket Creation Request: " + ticket);

        Ticket savedTicket = ticketService.createTicket(ticket);

        System.out.println("Ticket Created: " + savedTicket);

        return ResponseEntity.ok(savedTicket);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllTickets(@RequestParam String username) {
        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userOpt.get();
        List<Ticket> tickets;

        if (user.getRole() == User.Role.IT_SUPPORT) {
            tickets = ticketService.getAllTickets(); // IT Support sees all tickets
        } else {
            tickets = ticketService.getTicketsByUserId(user.getId()); // Employees see only their own
        }

        List<Map<String, Object>> sanitizedTickets = tickets.stream().map(ticket -> {
            Map<String, Object> ticketData = new HashMap<>();
            ticketData.put("id", ticket.getId());
            ticketData.put("title", ticket.getTitle());
            ticketData.put("description", ticket.getDescription());
            ticketData.put("priority", ticket.getPriority());
            ticketData.put("category", ticket.getCategory());
            ticketData.put("status", ticket.getStatus());
            ticketData.put("createdAt", ticket.getCreatedAt());

            return ticketData;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(sanitizedTickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Ticket> updateTicketStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String statusStr = request.get("status");
        String username = request.get("username");

        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userOpt.get();

        // Only IT Support Can Update Status
        if (user.getRole() != User.Role.IT_SUPPORT) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if (statusStr == null || username == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Ticket.Status status = Ticket.Status.valueOf(statusStr.toUpperCase());
            Ticket updatedTicket = ticketService.updateTicketStatus(id, status, username);
            return updatedTicket != null ? ResponseEntity.ok(updatedTicket) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/byUsername")
    public ResponseEntity<List<Ticket>> getTickets(@RequestParam String username) {
        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userOpt.get();
        List<Ticket> tickets;

        if (user.getRole() == User.Role.IT_SUPPORT) {
            tickets = ticketService.getAllTickets(); // IT Support gets all tickets
        } else {
            tickets = ticketService.getTicketsByUserId(user.getId()); // Employees only get their own tickets
        }

        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Ticket>> searchTickets(
            @RequestParam(required = false) Long ticketId,
            @RequestParam(required = false) String status,
            @RequestParam String username) {

        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userOpt.get();
        List<Ticket> tickets;

        if (ticketId != null) {
            // Search by Ticket ID
            Optional<Ticket> ticketOpt = ticketService.getTicketById(ticketId);
            if (ticketOpt.isPresent() && (user.getRole() == User.Role.IT_SUPPORT || ticketOpt.get().getUser().getId().equals(user.getId()))) {
                return ResponseEntity.ok(List.of(ticketOpt.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        if (status != null) {
            try {
                Ticket.Status ticketStatus = Ticket.Status.valueOf(status.toUpperCase());
                if (user.getRole() == User.Role.IT_SUPPORT) {
                    tickets = ticketService.getTicketsByStatus(ticketStatus);
                } else {
                    tickets = ticketService.getTicketsByUserAndStatus(user.getId(), ticketStatus);
                }
                return ResponseEntity.ok(tickets);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        return ResponseEntity.badRequest().build();
    }

}
