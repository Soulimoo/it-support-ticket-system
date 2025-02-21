package com.itsupport.ticketsystem.controller;

import com.itsupport.ticketsystem.model.AuditLog;
import com.itsupport.ticketsystem.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {
    @Autowired
    private AuditLogService auditLogService;

    @GetMapping("/{ticketId}")
    public ResponseEntity<List<AuditLog>> getLogsByTicketId(@PathVariable Long ticketId) {
        return ResponseEntity.ok(auditLogService.getLogsByTicketId(ticketId));
    }
}
