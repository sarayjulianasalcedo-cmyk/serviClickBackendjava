package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import com.sinquinto.serviclick.Notification.Application.NotificationService;
import com.sinquinto.serviclick.Notification.Domain.Notification;
import com.sinquinto.serviclick.Notification.Domain.NotificationRepository;
import com.sinquinto.serviclick.Notification.Domain.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationService service;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = Notification.builder()
                .id(1L)
                .userId(10L)
                .type(NotificationType.APPOINTMENT_REQUESTED)
                .title("Nueva solicitud")
                .message("Test message")
                .appointmentId(100L)
                .serviceOfferId(200L)
                .read(false)
                .build();
    }

    @Test
    @DisplayName("createNotification sin duplicado debe guardar y retornar")
    void createNotification_whenNoDuplicate_shouldSave() {
        when(repository.existsByAppointmentIdAndType(anyLong(), any())).thenReturn(false);
        when(repository.save(any())).thenReturn(notification);

        Notification result = service.createNotification(notification);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertFalse(result.getRead());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("createNotification con duplicado debe retornar null y no guardar")
    void createNotification_whenDuplicate_shouldReturnNull() {
        when(repository.existsByAppointmentIdAndType(anyLong(), any())).thenReturn(true);

        Notification result = service.createNotification(notification);

        assertNull(result);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("createNotification sin appointmentId no verifica duplicado")
    void createNotification_whenNoAppointmentId_shouldSkipDuplicateCheck() {
        Notification noAppNotif = Notification.builder()
                .userId(10L)
                .type(NotificationType.APPOINTMENT_ACCEPTED)
                .title("Test")
                .message("Test")
                .build();
        when(repository.save(any())).thenReturn(noAppNotif);

        Notification result = service.createNotification(noAppNotif);

        assertNotNull(result);
        verify(repository, never()).existsByAppointmentIdAndType(any(), any());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("findByUserId debe retornar lista de notificaciones del usuario")
    void findByUserId_shouldReturnList() {
        when(repository.findByUserId(10L)).thenReturn(List.of(notification));

        List<Notification> result = service.findByUserId(10L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findUnreadByUserId debe retornar solo las no leídas")
    void findUnreadByUserId_shouldReturnUnreadList() {
        when(repository.findByUserIdAndReadFalse(10L)).thenReturn(List.of(notification));

        List<Notification> result = service.findUnreadByUserId(10L);

        assertEquals(1, result.size());
        assertFalse(result.get(0).getRead());
    }

    @Test
    @DisplayName("markAsRead con ID existente debe marcar como leída")
    void markAsRead_whenExists_shouldMarkAndReturn() {
        when(repository.findById(1L)).thenReturn(Optional.of(notification));

        Notification result = service.markAsRead(1L);

        assertTrue(result.getRead());
        verify(repository).markAsRead(1L);
    }

    @Test
    @DisplayName("markAsRead con ID inexistente debe lanzar ResourceNotFoundException")
    void markAsRead_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.markAsRead(99L));
    }

    @Test
    @DisplayName("markAllAsRead debe llamar al repositorio con el userId")
    void markAllAsRead_shouldCallRepository() {
        service.markAllAsRead(10L);
        verify(repository).markAllAsReadByUserId(10L);
    }
}