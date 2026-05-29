package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Notification.Domain.Notification;
import com.sinquinto.serviclick.Notification.Domain.NotificationType;
import com.sinquinto.serviclick.Notification.Infrastructure.Entity.NotificationEntity;
import com.sinquinto.serviclick.Notification.Infrastructure.Mapper.NotificationMapper;
import com.sinquinto.serviclick.Notification.Infrastructure.Repository.NotificationJpaRepository;
import com.sinquinto.serviclick.Notification.Infrastructure.Repository.NotificationRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationRepositoryImplTest {

    @Mock
    private NotificationJpaRepository jpaRepository;

    private NotificationMapper mapper;
    private NotificationRepositoryImpl repository;

    private NotificationEntity entity;
    private Notification domain;

    @BeforeEach
    void setUp() {
        mapper = new NotificationMapper();
        repository = new NotificationRepositoryImpl(jpaRepository, mapper);

        entity = NotificationEntity.builder()
                .id(1L)
                .userId(10L)
                .type(NotificationType.APPOINTMENT_REQUESTED)
                .title("Test")
                .message("Test message")
                .read(false)
                .appointmentId(100L)
                .serviceOfferId(200L)
                .createdAt(LocalDateTime.now())
                .build();

        domain = Notification.builder()
                .id(1L)
                .userId(10L)
                .type(NotificationType.APPOINTMENT_REQUESTED)
                .title("Test")
                .message("Test message")
                .read(false)
                .appointmentId(100L)
                .serviceOfferId(200L)
                .createdAt(entity.getCreatedAt())
                .build();
    }

    @Test
    @DisplayName("save debe convertir, guardar y retornar dominio")
    void save_shouldConvertAndSave() {
        when(jpaRepository.save(any())).thenReturn(entity);

        Notification result = repository.save(domain);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertFalse(result.getRead());
        verify(jpaRepository).save(any());
    }

    @Test
    @DisplayName("findByUserId debe retornar lista de dominios")
    void findByUserId_shouldReturnList() {
        when(jpaRepository.findByUserIdOrderByCreatedAtDesc(10L)).thenReturn(List.of(entity));

        List<Notification> result = repository.findByUserId(10L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    @DisplayName("findByUserIdAndReadFalse debe retornar solo no leídas")
    void findByUserIdAndReadFalse_shouldReturnUnread() {
        when(jpaRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(10L))
                .thenReturn(List.of(entity));

        List<Notification> result = repository.findByUserIdAndReadFalse(10L);

        assertEquals(1, result.size());
        assertFalse(result.get(0).getRead());
    }

    @Test
    @DisplayName("findById con ID existente debe retornar Optional con dominio")
    void findById_whenExists_shouldReturnOptional() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Notification> result = repository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    @DisplayName("findById con ID inexistente debe retornar Optional vacío")
    void findById_whenNotFound_shouldReturnEmpty() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Notification> result = repository.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("markAsRead cuando existe la entidad debe marcarla como leída y guardar")
    void markAsRead_whenExists_shouldMarkAndSave() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(jpaRepository.save(any())).thenReturn(entity);

        repository.markAsRead(1L);

        assertTrue(entity.getRead());
        verify(jpaRepository).save(entity);
    }

    @Test
    @DisplayName("markAsRead con ID inexistente no debe llamar a save")
    void markAsRead_whenNotFound_shouldNotSave() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        repository.markAsRead(99L);

        verify(jpaRepository, never()).save(any());
    }

    @Test
    @DisplayName("markAllAsReadByUserId debe delegar al jpaRepository")
    void markAllAsReadByUserId_shouldDelegate() {
        repository.markAllAsReadByUserId(10L);
        verify(jpaRepository).markAllAsReadByUserId(10L);
    }

    @Test
    @DisplayName("existsByAppointmentIdAndType debe delegar al jpaRepository")
    void existsByAppointmentIdAndType_shouldDelegate() {
        when(jpaRepository.existsByAppointmentIdAndType(100L, NotificationType.APPOINTMENT_REQUESTED))
                .thenReturn(true);

        assertTrue(repository.existsByAppointmentIdAndType(100L, NotificationType.APPOINTMENT_REQUESTED));
    }
}