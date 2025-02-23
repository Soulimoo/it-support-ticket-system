package com.itsupport.ticketsystem.service;

import com.itsupport.ticketsystem.model.AuditLog;
import com.itsupport.ticketsystem.model.Ticket;
import com.itsupport.ticketsystem.repository.AuditLogRepository;
import com.itsupport.ticketsystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getTicketsByStatus(Ticket.Status status) {
        return ticketRepository.findByStatus(status);
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket updateTicketStatus(Long id, Ticket.Status status, String username) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();

            if (ticket.getStatus() != status) { // Log only when status changes
                AuditLog auditLog = AuditLog.builder()
                        .ticket(ticket)
                        .action(username + " changed status to " + status)
                        .timestamp(LocalDateTime.now())
                        .build();
                auditLogRepository.save(auditLog);
            }

            ticket.setStatus(status);
            return ticketRepository.save(ticket);
        }
        return null;
    }

    public List<Ticket> getTicketsByUserId(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    public List<Ticket> getTicketsByUserAndStatus(Long userId, Ticket.Status status) {
        return ticketRepository.findByUserIdAndStatus(userId, status);
    }

}
