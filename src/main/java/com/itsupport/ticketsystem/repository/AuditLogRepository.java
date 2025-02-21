package com.itsupport.ticketsystem.repository;

import com.itsupport.ticketsystem.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTicketId(Long ticketId);
}
