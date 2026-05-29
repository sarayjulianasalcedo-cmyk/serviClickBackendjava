package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Appointment.Domain.Appointment;
import com.sinquinto.serviclick.Appointment.Domain.AppointmentStatus;
import com.sinquinto.serviclick.Appointment.Infrastructure.Entity.AppointmentEntity;
import com.sinquinto.serviclick.Appointment.Infrastructure.Mapper.AppointmentMapper;
import com.sinquinto.serviclick.Appointment.Infrastructure.Mapper.AppointmentMapperImpl;
import com.sinquinto.serviclick.Appointment.Infrastructure.Repository.AppointmentJpaRepository;
import com.sinquinto.serviclick.Appointment.Infrastructure.Repository.AppointmentRepositoryImpl;
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
class AppointmentRepositoryImplTest {

    @Mock
    private AppointmentJpaRepository jpaRepository;

    private AppointmentMapper mapper;
    private AppointmentRepositoryImpl repository;

    private AppointmentEntity entity;
    private Appointment domain;

    @BeforeEach
    void setUp() {
        mapper = new AppointmentMapperImpl();
        repository = new AppointmentRepositoryImpl(jpaRepository, mapper);

        entity = AppointmentEntity.builder()
                .appointmentId(1L)
                .userId(10L)
                .serviceOfferId(20L)
                .date(LocalDateTime.now())
                .status(AppointmentStatus.PENDING)
                .build();

        domain = Appointment.builder()
                .appointmentId(1L)
                .userId(10L)
                .serviceOfferId(20L)
                .date(entity.getDate())
                .status(AppointmentStatus.PENDING)
                .build();
    }

    @Test
    @DisplayName("save debe convertir dominio a entidad, guardar y devolver dominio")
    void save_shouldConvertAndSave() {
        when(jpaRepository.save(any())).thenReturn(entity);

        Appointment result = repository.save(domain);

        assertNotNull(result);
        assertEquals(1L, result.getAppointmentId());
        assertEquals(AppointmentStatus.PENDING, result.getStatus());
        verify(jpaRepository).save(any());
    }

    @Test
    @DisplayName("findById con ID existente debe retornar dominio")
    void findById_whenExists_shouldReturnDomain() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Appointment result = repository.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getAppointmentId());
    }

    @Test
    @DisplayName("findById con ID inexistente debe retornar null")
    void findById_whenNotFound_shouldReturnNull() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        Appointment result = repository.findById(99L);

        assertNull(result);
    }

    @Test
    @DisplayName("findAll debe retornar lista de dominios mapeada")
    void findAll_shouldReturnMappedList() {
        when(jpaRepository.findAll()).thenReturn(List.of(entity));

        List<Appointment> result = repository.findAll();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getAppointmentId());
    }

    @Test
    @DisplayName("findByUserId debe retornar lista mapeada")
    void findByUserId_shouldReturnMappedList() {
        when(jpaRepository.findByUserId(10L)).thenReturn(List.of(entity));

        List<Appointment> result = repository.findByUserId(10L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByUserIdAndStatus debe retornar lista mapeada")
    void findByUserIdAndStatus_shouldReturnMappedList() {
        when(jpaRepository.findByUserIdAndStatus(10L, AppointmentStatus.PENDING))
                .thenReturn(List.of(entity));

        List<Appointment> result = repository.findByUserIdAndStatus(10L, AppointmentStatus.PENDING);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByServiceOfferId debe retornar lista mapeada")
    void findByServiceOfferId_shouldReturnMappedList() {
        when(jpaRepository.findByServiceOfferId(20L)).thenReturn(List.of(entity));

        List<Appointment> result = repository.findByServiceOfferId(20L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByServiceOfferIdIn debe retornar lista mapeada")
    void findByServiceOfferIdIn_shouldReturnMappedList() {
        when(jpaRepository.findByServiceOfferIdIn(anyList())).thenReturn(List.of(entity));

        List<Appointment> result = repository.findByServiceOfferIdIn(List.of(20L));

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("deleteById debe delegar al jpaRepository")
    void deleteById_shouldDelegate() {
        repository.deleteById(1L);
        verify(jpaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("countAppointments debe retornar el count del jpaRepository")
    void countAppointments_shouldReturnCount() {
        when(jpaRepository.count()).thenReturn(3L);

        assertEquals(3L, repository.countAppointments());
    }
}