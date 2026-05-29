package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Rating.Domain.Rating;
import com.sinquinto.serviclick.Rating.Infrastructure.Entity.RatingEntity;
import com.sinquinto.serviclick.Rating.Infrastructure.Mapper.RatingMapper;
import com.sinquinto.serviclick.Rating.Infrastructure.Mapper.RatingMapperImpl;
import com.sinquinto.serviclick.Rating.Infrastructure.Repository.RatingJpaRepository;
import com.sinquinto.serviclick.Rating.Infrastructure.Repository.RatingRepositoryImpl;
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
class RatingRepositoryImplTest {

    @Mock
    private RatingJpaRepository jpaRepository;

    private RatingMapper mapper;
    private RatingRepositoryImpl repository;

    private RatingEntity entity;
    private Rating domain;

    @BeforeEach
    void setUp() {
        mapper = new RatingMapperImpl();
        repository = new RatingRepositoryImpl(jpaRepository, mapper);

        entity = RatingEntity.builder()
                .ratingId(1L)
                .userId(10L)
                .serviceOfferId(20L)
                .appointmentId(100L)
                .score(5)
                .comment("Excelente")
                .date(LocalDateTime.now())
                .build();

        domain = Rating.builder()
                .ratingId(1L)
                .userId(10L)
                .serviceOfferId(20L)
                .appointmentId(100L)
                .score(5)
                .comment("Excelente")
                .date(entity.getDate())
                .build();
    }

    @Test
    @DisplayName("save debe convertir, guardar y retornar dominio")
    void save_shouldConvertAndSave() {
        when(jpaRepository.save(any())).thenReturn(entity);

        Rating result = repository.save(domain);

        assertNotNull(result);
        assertEquals(1L, result.getRatingId());
        assertEquals(5, result.getScore());
        verify(jpaRepository).save(any());
    }

    @Test
    @DisplayName("findById con ID existente debe retornar dominio")
    void findById_whenExists_shouldReturn() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Rating result = repository.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getRatingId());
    }

    @Test
    @DisplayName("findById con ID inexistente debe retornar null")
    void findById_whenNotFound_shouldReturnNull() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        Rating result = repository.findById(99L);

        assertNull(result);
    }

    @Test
    @DisplayName("findAll debe retornar lista mapeada")
    void findAll_shouldReturnMappedList() {
        when(jpaRepository.findAll()).thenReturn(List.of(entity));

        List<Rating> result = repository.findAll();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByServiceOfferId debe retornar lista mapeada")
    void findByServiceOfferId_shouldReturnMappedList() {
        when(jpaRepository.findByServiceOfferId(20L)).thenReturn(List.of(entity));

        List<Rating> result = repository.findByServiceOfferId(20L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByUserId debe retornar lista mapeada")
    void findByUserId_shouldReturnMappedList() {
        when(jpaRepository.findByUserId(10L)).thenReturn(List.of(entity));

        List<Rating> result = repository.findByUserId(10L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("existsByAppointmentId debe delegar al jpaRepository")
    void existsByAppointmentId_shouldDelegate() {
        when(jpaRepository.existsByAppointmentId(100L)).thenReturn(true);

        assertTrue(repository.existsByAppointmentId(100L));
    }

    @Test
    @DisplayName("deleteById debe delegar al jpaRepository")
    void deleteById_shouldDelegate() {
        repository.deleteById(1L);
        verify(jpaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("countRatings debe retornar el count del jpaRepository")
    void countRatings_shouldReturnCount() {
        when(jpaRepository.count()).thenReturn(5L);

        assertEquals(5L, repository.countRatings());
    }
}