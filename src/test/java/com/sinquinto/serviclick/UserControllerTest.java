package com.sinquinto.serviclick;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinquinto.serviclick.User.Application.UserService;
import com.sinquinto.serviclick.User.Domain.Role;
import com.sinquinto.serviclick.User.Domain.User;
import com.sinquinto.serviclick.User.Infrastructure.DTO.UserDTO;
import com.sinquinto.serviclick.User.Infrastructure.UserController;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        user = User.builder()
                .userId(1L).name("Carlos").lastName("Gomez")
                .email("carlos@test.com").password("pass")
                .phoneNumber("123").role(Role.CUSTOMER)
                .build();

        userDTO = new UserDTO(1L, "Carlos", "Gomez", "carlos@test.com", "123", Role.CUSTOMER);
    }

    @Test
    @DisplayName("POST /api/users debe crear usuario y retornar 201")
    void save_shouldReturn201() throws Exception {
        when(service.saveUser(any())).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        verify(service).saveUser(any());
    }

    @Test
    @DisplayName("GET /api/users debe retornar lista de DTOs 200 OK")
    void getAll_shouldReturn200() throws Exception {
        when(service.findAllUsers()).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());

        verify(service).findAllUsers();
    }

    @Test
    @DisplayName("GET /api/users/{id} debe retornar DTO 200 OK")
    void findById_shouldReturn200() throws Exception {
        when(service.findUserDTOById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());

        verify(service).findUserDTOById(1L);
    }

    @Test
    @DisplayName("GET /api/users/email/{email} debe retornar usuario 200 OK")
    void findByEmail_shouldReturn200() throws Exception {
        when(service.findUserByEmail("carlos@test.com")).thenReturn(user);

        mockMvc.perform(get("/api/users/email/carlos@test.com"))
                .andExpect(status().isOk());

        verify(service).findUserByEmail("carlos@test.com");
    }

    @Test
    @DisplayName("GET /api/users/count debe retornar conteo 200 OK")
    void countUsers_shouldReturn200() throws Exception {
        when(service.countUsers()).thenReturn(5L);

        mockMvc.perform(get("/api/users/count"))
                .andExpect(status().isOk());

        verify(service).countUsers();
    }

    @Test
    @DisplayName("PUT /api/users debe actualizar usuario 200 OK")
    void update_shouldReturn200() throws Exception {
        when(service.updateUser(anyLong(), any())).thenReturn(user);

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        verify(service).updateUser(anyLong(), any());
    }

    @Test
    @DisplayName("DELETE /api/users/{id} debe eliminar usuario 200 OK")
    void delete_shouldReturn200() throws Exception {
        doNothing().when(service).deleteUserById(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(service).deleteUserById(1L);
    }
}