package com.itsupport.ticketsystem.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.itsupport.ticketsystem.model.AuditLog;
import com.itsupport.ticketsystem.service.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class AuditLogControllerTest {

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AuditLogController auditLogController;

    private AuditLog mockAuditLog;

    @BeforeEach
    void setUp() {
        mockAuditLog = new AuditLog(1L, null, "Comment added", null);
    }

    @Test
    void testGetLogsByTicketId() {
        when(auditLogService.getLogsByTicketId(1L)).thenReturn(Arrays.asList(mockAuditLog));
        ResponseEntity<List<AuditLog>> response = auditLogController.getLogsByTicketId(1L);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().size());
    }
}
