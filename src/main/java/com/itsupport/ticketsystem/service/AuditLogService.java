package com.itsupport.ticketsystem.service;

import com.itsupport.ticketsystem.model.AuditLog;
import com.itsupport.ticketsystem.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogService {
    @Autowired
    private AuditLogRepository auditLogRepository;

    public AuditLog createAuditLog(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getLogsByTicketId(Long ticketId) {
        return auditLogRepository.findByTicketId(ticketId);
    }
}
