package com.sinquinto.serviclick;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinquinto.serviclick.Log.Application.FrontendLogService;
import com.sinquinto.serviclick.Log.Domain.FrontendLog;
import com.sinquinto.serviclick.Log.Infrastructure.FrontendLogController;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FrontendLogControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private FrontendLogService service;

    @InjectMocks
    private FrontendLogController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /api/logs debe guardar logs y retornar 200 OK")
    void saveLogs_shouldReturn200() throws Exception {
        doNothing().when(service).saveLogs(anyList());

        mockMvc.perform(post("/api/logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isOk());

        verify(service).saveLogs(anyList());
    }

    @Test
    @DisplayName("GET /api/logs debe retornar lista de logs 200 OK")
    void getLogs_shouldReturn200() throws Exception {
        when(service.getLogs()).thenReturn(List.of(new FrontendLog()));

        mockMvc.perform(get("/api/logs"))
                .andExpect(status().isOk());

        verify(service).getLogs();
    }
}