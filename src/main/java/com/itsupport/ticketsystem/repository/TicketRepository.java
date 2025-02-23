package com.itsupport.ticketsystem.repository;

import com.itsupport.ticketsystem.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(Ticket.Status status);
    List<Ticket> findByUserId(Long userId);

    List<Ticket> findByUserIdAndStatus(Long userId, Ticket.Status status);
}
