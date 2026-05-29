package com.sinquinto.serviclick;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sinquinto.serviclick.Rating.Application.RatingService;
import com.sinquinto.serviclick.Rating.Domain.Rating;
import com.sinquinto.serviclick.Rating.Infrastructure.DTO.RatingDTO;
import com.sinquinto.serviclick.Rating.Infrastructure.RatingController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RatingControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private RatingService service;

    @InjectMocks
    private RatingController controller;

    private Rating rating;
    private RatingDTO ratingDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        rating = Rating.builder()
                .ratingId(1L).userId(10L).serviceOfferId(20L)
                .appointmentId(100L).score(5).comment("Excelente")
                .date(LocalDateTime.now())
                .build();

        ratingDTO = new RatingDTO("Juan Perez", 5, "Excelente", LocalDateTime.now());
    }

    @Test
    @DisplayName("POST /api/ratings debe crear rating y retornar 201")
    void save_shouldReturn201() throws Exception {
        when(service.saveRating(any())).thenReturn(rating);

        mockMvc.perform(post("/api/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rating)))
                .andExpect(status().isCreated());

        verify(service).saveRating(any());
    }

    @Test
    @DisplayName("GET /api/ratings debe retornar lista 200 OK")
    void getAll_shouldReturn200() throws Exception {
        when(service.findAllRatings()).thenReturn(List.of(rating));

        mockMvc.perform(get("/api/ratings"))
                .andExpect(status().isOk());

        verify(service).findAllRatings();
    }

    @Test
    @DisplayName("GET /api/ratings/{id} debe retornar rating 200 OK")
    void findById_shouldReturn200() throws Exception {
        when(service.findRatingById(1L)).thenReturn(rating);

        mockMvc.perform(get("/api/ratings/1"))
                .andExpect(status().isOk());

        verify(service).findRatingById(1L);
    }

    @Test
    @DisplayName("GET /api/ratings/service-offer/{serviceOfferId} debe retornar DTOs 200 OK")
    void findByServiceOffer_shouldReturn200() throws Exception {
        when(service.findRatingsDTOByServiceOffer(20L)).thenReturn(List.of(ratingDTO));

        mockMvc.perform(get("/api/ratings/service-offer/20"))
                .andExpect(status().isOk());

        verify(service).findRatingsDTOByServiceOffer(20L);
    }

    @Test
    @DisplayName("GET /api/ratings/user/{userId} debe retornar lista 200 OK")
    void findByUser_shouldReturn200() throws Exception {
        when(service.findRatingsByUser(10L)).thenReturn(List.of(rating));

        mockMvc.perform(get("/api/ratings/user/10"))
                .andExpect(status().isOk());

        verify(service).findRatingsByUser(10L);
    }

    @Test
    @DisplayName("GET /api/ratings/count debe retornar conteo 200 OK")
    void count_shouldReturn200() throws Exception {
        when(service.countRatings()).thenReturn(3L);

        mockMvc.perform(get("/api/ratings/count"))
                .andExpect(status().isOk());

        verify(service).countRatings();
    }

    @Test
    @DisplayName("PUT /api/ratings/{id} debe actualizar rating 200 OK")
    void update_shouldReturn200() throws Exception {
        when(service.updateRating(anyLong(), any())).thenReturn(rating);

        mockMvc.perform(put("/api/ratings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rating)))
                .andExpect(status().isOk());

        verify(service).updateRating(anyLong(), any());
    }

    @Test
    @DisplayName("DELETE /api/ratings/{id} debe eliminar rating 200 OK")
    void delete_shouldReturn200() throws Exception {
        doNothing().when(service).deleteRatingById(1L);

        mockMvc.perform(delete("/api/ratings/1"))
                .andExpect(status().isOk());

        verify(service).deleteRatingById(1L);
    }
}