package com.itsupport.ticketsystem.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.itsupport.ticketsystem.model.Ticket;
import com.itsupport.ticketsystem.model.User;
import com.itsupport.ticketsystem.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Ticket mockTicket;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User(1L, "testUser", "password", User.Role.EMPLOYEE);
        mockTicket = new Ticket(1L, "Test Ticket", "Description", Ticket.Priority.MEDIUM, Ticket.Category.SOFTWARE, Ticket.Status.NEW, null, mockUser);
    }

    @Test
    void testCreateTicket() {
        when(ticketRepository.save(any(Ticket.class))).thenReturn(mockTicket);
        Ticket createdTicket = ticketService.createTicket(mockTicket);
        assertNotNull(createdTicket);
        assertEquals("Test Ticket", createdTicket.getTitle());
    }

    @Test
    void testGetTicketById() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(mockTicket));
        Optional<Ticket> retrievedTicket = ticketService.getTicketById(1L);
        assertTrue(retrievedTicket.isPresent());
        assertEquals("Test Ticket", retrievedTicket.get().getTitle());
    }
}
