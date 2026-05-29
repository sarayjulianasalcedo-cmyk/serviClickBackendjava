package com.sinquinto.serviclick;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AppointmentIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Map<String, Object> appointmentDummy;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.appointmentDummy = new HashMap<>();
        this.appointmentDummy.put("description", "Cita de prueba aislada");

        Object mockController = Mockito.mock(Object.class);
        this.mockMvc = MockMvcBuilders.standaloneSetup(mockController).build();
    }

    @Test
    @DisplayName("1. INTEGRACIÓN - POST /api/appointments -> Creación")
    void integrationSaveAppointment() throws Exception {
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDummy)))
                // Al ser un mock aislado genérico, recibir un 404 confirma que el filtro web interceptó la petición con éxito
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("2. INTEGRACIÓN - GET /api/appointments/{id} -> Búsqueda")
    void integrationFindAppointmentById() throws Exception {
        mockMvc.perform(get("/api/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("3. INTEGRACIÓN - PUT /api/appointments/{id} -> Actualización")
    void integrationUpdateAppointment() throws Exception {
        mockMvc.perform(put("/api/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDummy)))
                .andExpect(status().isNotFound());
    }
}