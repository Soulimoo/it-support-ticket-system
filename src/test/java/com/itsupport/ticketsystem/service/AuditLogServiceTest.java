package com.itsupport.ticketsystem.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.itsupport.ticketsystem.model.AuditLog;
import com.itsupport.ticketsystem.model.Ticket;
import com.itsupport.ticketsystem.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogService auditLogService;

    private AuditLog mockAuditLog;
    private Ticket mockTicket;

    @BeforeEach
    void setUp() {
        mockTicket = new Ticket();
        mockTicket.setId(1L);
        mockAuditLog = new AuditLog(1L, mockTicket, "Test Action", null);
    }

    @Test
    void testGetLogsByTicketId() {
        when(auditLogRepository.findByTicketId(1L)).thenReturn(Collections.singletonList(mockAuditLog));
        List<AuditLog> logs = auditLogService.getLogsByTicketId(1L);
        assertFalse(logs.isEmpty());
        assertEquals("Test Action", logs.get(0).getAction());
    }
}
