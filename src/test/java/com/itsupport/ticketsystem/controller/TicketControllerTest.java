package com.itsupport.ticketsystem.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.itsupport.ticketsystem.model.Ticket;
import com.itsupport.ticketsystem.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    private Ticket mockTicket;

    @BeforeEach
    void setUp() {
        mockTicket = new Ticket(1L, "Test Ticket", "Description", Ticket.Priority.MEDIUM, Ticket.Category.SOFTWARE, Ticket.Status.NEW, null, null);
    }

    @Test
    void testGetTicketById() {
        when(ticketService.getTicketById(1L)).thenReturn(Optional.of(mockTicket));
        ResponseEntity<Ticket> response = ticketController.getTicketById(1L);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Test Ticket", response.getBody().getTitle());
    }
}
