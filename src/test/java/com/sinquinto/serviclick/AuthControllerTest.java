package com.sinquinto.serviclick;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinquinto.serviclick.Auth.Application.AuthService;
import com.sinquinto.serviclick.Auth.Infrastructure.AuthController;
import com.sinquinto.serviclick.Auth.Infrastructure.AuthResponse;
import com.sinquinto.serviclick.Auth.Infrastructure.GooglePendingUserResponse;
import com.sinquinto.serviclick.User.Domain.Role;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /api/auth/login debe retornar 200 OK con AuthResponse")
    void login_shouldReturn200() throws Exception {
        AuthResponse authResponse = AuthResponse.builder()
                .token("jwt.token").userId(1L).email("user@test.com").role(Role.CUSTOMER).build();
        when(authService.login(any())).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@test.com\",\"password\":\"pass123\"}"))
                .andExpect(status().isOk());

        verify(authService).login(any());
    }

    @Test
    @DisplayName("POST /api/auth/register debe retornar 200 OK con AuthResponse")
    void register_shouldReturn200() throws Exception {
        AuthResponse authResponse = AuthResponse.builder()
                .token("jwt.token").userId(2L).email("new@test.com").role(Role.SALESPERSON).build();
        when(authService.register(any())).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"new@test.com\",\"password\":\"pass\",\"name\":\"Test\",\"lastName\":\"User\",\"role\":\"SALESPERSON\"}"))
                .andExpect(status().isOk());

        verify(authService).register(any());
    }

    @Test
    @DisplayName("POST /api/auth/google-login cuando retorna AuthResponse debe retornar 200")
    void googleLogin_whenAuthResponse_shouldReturn200() throws Exception {
        AuthResponse authResponse = AuthResponse.builder()
                .token("google.jwt.token").userId(3L).email("google@test.com").build();
        when(authService.googleLogin(any())).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/google-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idToken\":\"test.google.id.token\"}"))
                .andExpect(status().isOk());

        verify(authService).googleLogin(any());
    }

    @Test
    @DisplayName("POST /api/auth/google-login cuando retorna GooglePendingUserResponse debe retornar 202")
    void googleLogin_whenPendingUser_shouldReturn202() throws Exception {
        GooglePendingUserResponse pending = GooglePendingUserResponse.builder()
                .email("pending@test.com").name("Pending").lastName("User")
                .requiresRoleSelection(true).build();
        when(authService.googleLogin(any())).thenReturn(pending);

        mockMvc.perform(post("/api/auth/google-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idToken\":\"test.google.id.token\"}"))
                .andExpect(status().isAccepted());

        verify(authService).googleLogin(any());
    }

    @Test
    @DisplayName("POST /api/auth/google-register debe retornar 200 OK con AuthResponse")
    void googleRegister_shouldReturn200() throws Exception {
        AuthResponse authResponse = AuthResponse.builder()
                .token("google.reg.jwt").userId(4L).email("reg@test.com").build();
        when(authService.googleRegister(any())).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/google-register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idToken\":\"test.google.id.token\",\"role\":\"CUSTOMER\",\"name\":\"Test\",\"lastName\":\"Reg\"}"))
                .andExpect(status().isOk());

        verify(authService).googleRegister(any());
    }
}
