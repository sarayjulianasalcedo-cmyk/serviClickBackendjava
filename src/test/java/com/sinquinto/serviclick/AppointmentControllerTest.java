package com.sinquinto.serviclick;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinquinto.serviclick.Appointment.Application.AppointmentService;
import com.sinquinto.serviclick.Appointment.Domain.Appointment;
import com.sinquinto.serviclick.Appointment.Infrastructure.AppointmentController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class) // Levanta solo Mockito, ignorando la base de datos y el servidor
class AppointmentControllerTest {

    private MockMvc mockMvc;

    @Mock // Cambiado de @MockitoBean a @Mock estándar de Mockito
    private AppointmentService appointmentService;

    @InjectMocks // Inyecta automáticamente el servicio simulado dentro del controlador
    private AppointmentController appointmentController;

    private ObjectMapper objectMapper;
    private Appointment sampleAppointment;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        // Construye el entorno Web simulado solo para este controlador
        this.mockMvc = MockMvcBuilders.standaloneSetup(appointmentController).build();

        sampleAppointment = new Appointment();
    }

    @Test
    @DisplayName("1. POST /api/appointments -> Debería crear una cita exitosamente (201 CREATED)")
    void saveAppointment_ShouldReturnCreated() throws Exception {
        Mockito.when(appointmentService.saveAppointment(any(Appointment.class)))
                .thenReturn(sampleAppointment);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleAppointment)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("2. GET /api/appointments/{id} -> Debería retornar una cita por ID (200 OK)")
    void findById_ShouldReturnAppointment() throws Exception {
        Mockito.when(appointmentService.findAppointmentById(1L))
                .thenReturn(sampleAppointment);

        mockMvc.perform(get("/api/appointments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("3. PUT /api/appointments/{id} -> Debería actualizar una cita correctamente (200 OK)")
    void updateAppointment_ShouldReturnUpdatedAppointment() throws Exception {
        Mockito.when(appointmentService.updateAppointment(eq(1L), any(Appointment.class)))
                .thenReturn(sampleAppointment);

        mockMvc.perform(put("/api/appointments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleAppointment)))
                .andExpect(status().isOk());
    }
}