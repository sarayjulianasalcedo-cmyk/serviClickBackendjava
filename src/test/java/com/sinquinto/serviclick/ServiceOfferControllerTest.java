package com.sinquinto.serviclick;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sinquinto.serviclick.ServiceOffer.Application.ServiceOfferService;
import com.sinquinto.serviclick.ServiceOffer.Domain.ServiceOffer;
import com.sinquinto.serviclick.ServiceOffer.Infrastructure.ServiceOfferController;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ServiceOfferControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ServiceOfferService service;

    @InjectMocks
    private ServiceOfferController controller;

    private ServiceOffer serviceOffer;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        serviceOffer = ServiceOffer.builder()
                .serviceOfferId(1L).sellerId(10L)
                .title("Pintura").description("Pintamos").price(BigDecimal.valueOf(100.0))
                .estimatedDuration(3).category("Hogar")
                .build();
    }

    @Test
    @DisplayName("POST /api/service-offers debe crear y retornar 201")
    void save_shouldReturn201() throws Exception {
        when(service.saveServiceOffer(any())).thenReturn(serviceOffer);

        mockMvc.perform(post("/api/service-offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceOffer)))
                .andExpect(status().isCreated());

        verify(service).saveServiceOffer(any());
    }

    @Test
    @DisplayName("GET /api/service-offers debe retornar lista 200 OK")
    void getAll_shouldReturn200() throws Exception {
        when(service.findAllServiceOffers()).thenReturn(List.of(serviceOffer));

        mockMvc.perform(get("/api/service-offers"))
                .andExpect(status().isOk());

        verify(service).findAllServiceOffers();
    }

    @Test
    @DisplayName("GET /api/service-offers/{id} debe retornar servicio 200 OK")
    void findById_shouldReturn200() throws Exception {
        when(service.findServiceOfferById(1L)).thenReturn(serviceOffer);

        mockMvc.perform(get("/api/service-offers/1"))
                .andExpect(status().isOk());

        verify(service).findServiceOfferById(1L);
    }

    @Test
    @DisplayName("GET /api/service-offers/seller/{sellerId} debe retornar lista 200 OK")
    void findBySeller_shouldReturn200() throws Exception {
        when(service.findServiceOffersBySeller(10L)).thenReturn(List.of(serviceOffer));

        mockMvc.perform(get("/api/service-offers/seller/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sellerId").value(10));

        verify(service).findServiceOffersBySeller(10L);
    }

    @Test
    @DisplayName("GET /api/service-offers/seller/{sellerId} con vendedor sin servicios debe retornar [] 200 OK")
    void findBySeller_whenNoServices_shouldReturnEmptyList() throws Exception {
        when(service.findServiceOffersBySeller(99L)).thenReturn(List.of());

        mockMvc.perform(get("/api/service-offers/seller/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(service).findServiceOffersBySeller(99L);
    }

    @Test
    @DisplayName("GET /api/service-offers/count debe retornar conteo 200 OK")
    void count_shouldReturn200() throws Exception {
        when(service.countServiceOffers()).thenReturn(8L);

        mockMvc.perform(get("/api/service-offers/count"))
                .andExpect(status().isOk());

        verify(service).countServiceOffers();
    }

    @Test
    @DisplayName("PUT /api/service-offers/{id} debe actualizar y retornar 200 OK")
    void update_shouldReturn200() throws Exception {
        when(service.updateServiceOffer(anyLong(), any())).thenReturn(serviceOffer);

        mockMvc.perform(put("/api/service-offers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceOffer)))
                .andExpect(status().isOk());

        verify(service).updateServiceOffer(anyLong(), any());
    }

    @Test
    @DisplayName("DELETE /api/service-offers/{id} debe eliminar 200 OK")
    void delete_shouldReturn200() throws Exception {
        doNothing().when(service).deleteServiceOfferById(1L);

        mockMvc.perform(delete("/api/service-offers/1"))
                .andExpect(status().isOk());

        verify(service).deleteServiceOfferById(1L);
    }
}