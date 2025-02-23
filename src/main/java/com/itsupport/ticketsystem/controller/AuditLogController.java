package com.itsupport.ticketsystem.controller;

import com.itsupport.ticketsystem.model.AuditLog;
import com.itsupport.ticketsystem.model.Ticket;
import com.itsupport.ticketsystem.model.User;
import com.itsupport.ticketsystem.service.AuditLogService;
import com.itsupport.ticketsystem.service.TicketService;
import com.itsupport.ticketsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {
    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @GetMapping("/{ticketId}")
    public ResponseEntity<List<AuditLog>> getLogsByTicketId(@PathVariable Long ticketId) {
        return ResponseEntity.ok(auditLogService.getLogsByTicketId(ticketId));
    }


    @PostMapping
    public ResponseEntity<AuditLog> addComment(@RequestBody Map<String, String> request) {
        Long ticketId = Long.parseLong(request.get("ticketId"));
        String comment = request.get("comment");

        Optional<Ticket> ticketOpt = ticketService.getTicketById(ticketId);
        if (ticketOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AuditLog auditLog = AuditLog.builder()
                .ticket(ticketOpt.get())
                .action("Comment: " + comment)
                .timestamp(LocalDateTime.now())
                .build();

        auditLog = auditLogService.createAuditLog(auditLog);
        return ResponseEntity.ok(auditLog);
    }

}
