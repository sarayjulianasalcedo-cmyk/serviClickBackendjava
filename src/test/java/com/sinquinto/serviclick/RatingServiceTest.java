package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Exception.Domain.ResourceDuplicateException;
import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import com.sinquinto.serviclick.Rating.Application.RatingService;
import com.sinquinto.serviclick.Rating.Domain.Rating;
import com.sinquinto.serviclick.Rating.Domain.RatingRepository;
import com.sinquinto.serviclick.Rating.Infrastructure.DTO.RatingDTO;
import com.sinquinto.serviclick.User.Application.UserService;
import com.sinquinto.serviclick.User.Domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock private RatingRepository repository;
    @Mock private UserService userService;

    private RatingService service;
    private Rating rating;

    @BeforeEach
    void setUp() {
        service = new RatingService(repository, userService);

        rating = Rating.builder()
                .ratingId(1L)
                .userId(10L)
                .serviceOfferId(20L)
                .appointmentId(100L)
                .score(5)
                .comment("Excelente servicio")
                .date(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("saveRating sin duplicado debe guardar y retornar")
    void saveRating_whenNoDuplicate_shouldSave() {
        when(repository.existsByAppointmentId(100L)).thenReturn(false);
        when(repository.save(any())).thenReturn(rating);

        Rating result = service.saveRating(rating);

        assertNotNull(result);
        assertNotNull(result.getDate());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("saveRating con duplicado debe lanzar ResourceDuplicateException")
    void saveRating_whenDuplicate_shouldThrow() {
        when(repository.existsByAppointmentId(100L)).thenReturn(true);

        assertThrows(ResourceDuplicateException.class, () -> service.saveRating(rating));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("saveRating sin appointmentId no verifica duplicado")
    void saveRating_whenNoAppointmentId_shouldSaveWithoutDuplicateCheck() {
        Rating noAppRating = Rating.builder()
                .userId(10L).serviceOfferId(20L).score(4).comment("Bueno").build();
        when(repository.save(any())).thenReturn(noAppRating);

        Rating result = service.saveRating(noAppRating);

        assertNotNull(result);
        verify(repository, never()).existsByAppointmentId(any());
    }

    @Test
    @DisplayName("existsRatingForAppointment debe retornar el valor del repositorio")
    void existsRatingForAppointment_shouldDelegate() {
        when(repository.existsByAppointmentId(100L)).thenReturn(true);
        assertTrue(service.existsRatingForAppointment(100L));

        when(repository.existsByAppointmentId(200L)).thenReturn(false);
        assertFalse(service.existsRatingForAppointment(200L));
    }

    @Test
    @DisplayName("findRatingById con ID existente debe retornar el rating")
    void findRatingById_whenExists_shouldReturn() {
        when(repository.findById(1L)).thenReturn(rating);

        Rating result = service.findRatingById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getRatingId());
    }

    @Test
    @DisplayName("findRatingById con ID inexistente debe lanzar ResourceNotFoundException")
    void findRatingById_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.findRatingById(99L));
    }

    @Test
    @DisplayName("findAllRatings debe retornar lista completa")
    void findAllRatings_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(rating));

        List<Rating> result = service.findAllRatings();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findRatingsByServiceOffer debe retornar ratings del servicio")
    void findRatingsByServiceOffer_shouldReturnList() {
        when(repository.findByServiceOfferId(20L)).thenReturn(List.of(rating));

        List<Rating> result = service.findRatingsByServiceOffer(20L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findRatingsDTOByServiceOffer debe retornar DTOs con nombre del cliente")
    void findRatingsDTOByServiceOffer_shouldReturnDTOsWithClientName() {
        when(repository.findByServiceOfferId(20L)).thenReturn(List.of(rating));
        when(userService.findUserById(10L)).thenReturn(
                User.builder().userId(10L).name("Juan").lastName("Perez").build());

        List<RatingDTO> result = service.findRatingsDTOByServiceOffer(20L);

        assertEquals(1, result.size());
        assertEquals("Juan Perez", result.get(0).clientName());
        assertEquals(5, result.get(0).rating());
        assertEquals("Excelente servicio", result.get(0).comment());
    }

    @Test
    @DisplayName("findRatingsDTOByServiceOffer cuando usuario no existe usa nombre 'Cliente'")
    void findRatingsDTOByServiceOffer_whenUserNotFound_shouldUseDefaultName() {
        when(repository.findByServiceOfferId(20L)).thenReturn(List.of(rating));
        when(userService.findUserById(10L)).thenThrow(new RuntimeException("not found"));

        List<RatingDTO> result = service.findRatingsDTOByServiceOffer(20L);

        assertEquals(1, result.size());
        assertEquals("Cliente", result.get(0).clientName());
    }

    @Test
    @DisplayName("findRatingsByUser debe retornar ratings del usuario")
    void findRatingsByUser_shouldReturnList() {
        when(repository.findByUserId(10L)).thenReturn(List.of(rating));

        List<Rating> result = service.findRatingsByUser(10L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("updateRating con ID existente debe actualizar score y comment")
    void updateRating_whenExists_shouldUpdateAndReturn() {
        when(repository.findById(1L)).thenReturn(rating);
        when(repository.save(any())).thenReturn(rating);
        Rating updateData = Rating.builder().score(3).comment("Regular").build();

        Rating result = service.updateRating(1L, updateData);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("updateRating con ID inexistente debe lanzar excepción")
    void updateRating_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateRating(99L, rating));
    }

    @Test
    @DisplayName("deleteRatingById con ID existente debe eliminar")
    void deleteRatingById_whenExists_shouldDelete() {
        when(repository.findById(1L)).thenReturn(rating);

        service.deleteRatingById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteRatingById con ID inexistente debe lanzar excepción")
    void deleteRatingById_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteRatingById(99L));
    }

    @Test
    @DisplayName("countRatings debe retornar el conteo del repositorio")
    void countRatings_shouldReturnCount() {
        when(repository.countRatings()).thenReturn(10L);

        assertEquals(10L, service.countRatings());
    }
}