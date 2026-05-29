package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Notification.Application.NotificationService;
import com.sinquinto.serviclick.Notification.Domain.Notification;
import com.sinquinto.serviclick.Notification.Domain.NotificationType;
import com.sinquinto.serviclick.Notification.Infrastructure.NotificationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NotificationService service;

    @InjectMocks
    private NotificationController controller;

    private Notification notification;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        notification = Notification.builder()
                .id(1L).userId(10L)
                .type(NotificationType.APPOINTMENT_REQUESTED)
                .title("Test").message("Message").read(false)
                .build();
    }

    @Test
    @DisplayName("GET /api/notifications/user/{userId} debe retornar lista 200 OK")
    void findByUser_shouldReturn200() throws Exception {
        when(service.findByUserId(10L)).thenReturn(List.of(notification));

        mockMvc.perform(get("/api/notifications/user/10"))
                .andExpect(status().isOk());

        verify(service).findByUserId(10L);
    }

    @Test
    @DisplayName("GET /api/notifications/user/{userId}/unread debe retornar no leídas 200 OK")
    void findUnreadByUser_shouldReturn200() throws Exception {
        when(service.findUnreadByUserId(10L)).thenReturn(List.of(notification));

        mockMvc.perform(get("/api/notifications/user/10/unread"))
                .andExpect(status().isOk());

        verify(service).findUnreadByUserId(10L);
    }

    @Test
    @DisplayName("PATCH /api/notifications/{id}/read debe marcar como leída 200 OK")
    void markAsRead_shouldReturn200() throws Exception {
        notification.setRead(true);
        when(service.markAsRead(1L)).thenReturn(notification);

        mockMvc.perform(patch("/api/notifications/1/read"))
                .andExpect(status().isOk());

        verify(service).markAsRead(1L);
    }

    @Test
    @DisplayName("PATCH /api/notifications/user/{userId}/read-all debe retornar 204 No Content")
    void markAllAsRead_shouldReturn204() throws Exception {
        doNothing().when(service).markAllAsRead(anyLong());

        mockMvc.perform(patch("/api/notifications/user/10/read-all"))
                .andExpect(status().isNoContent());

        verify(service).markAllAsRead(10L);
    }
}