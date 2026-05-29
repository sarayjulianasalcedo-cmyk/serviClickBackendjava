package com.sinquinto.serviclick;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.sinquinto.serviclick.Auth.Application.AuthService;
import com.sinquinto.serviclick.Auth.Infrastructure.AuthResponse;
import com.sinquinto.serviclick.Auth.Infrastructure.GoogleLoginRequest;
import com.sinquinto.serviclick.Auth.Infrastructure.GooglePendingUserResponse;
import com.sinquinto.serviclick.Auth.Infrastructure.GoogleRegisterRequest;
import com.sinquinto.serviclick.Auth.Infrastructure.LoginRequest;
import com.sinquinto.serviclick.Auth.Infrastructure.RegisterRequest;
import com.sinquinto.serviclick.Config.Application.JwtService;
import com.sinquinto.serviclick.User.Application.UserService;
import com.sinquinto.serviclick.User.Domain.Role;
import com.sinquinto.serviclick.User.Domain.User;
import com.sinquinto.serviclick.User.Infrastructure.Entity.UserEntity;
import com.sinquinto.serviclick.User.Infrastructure.Mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserService userService;
    @Mock private JwtService jwtService;
    @Mock private UserMapper userMapper;
    @Mock private AuthenticationManager authenticationManager;

    private AuthService authService;

    private User user;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userService, jwtService, userMapper, authenticationManager);
        ReflectionTestUtils.setField(authService, "googleClientId", "test-client-id");

        user = User.builder()
                .userId(1L)
                .name("Ana")
                .lastName("Torres")
                .email("ana@test.com")
                .password("encoded")
                .phoneNumber("111222333")
                .role(Role.CUSTOMER)
                .registerDate(LocalDateTime.now())
                .build();

        userEntity = UserEntity.builder()
                .userId(1L)
                .name("Ana")
                .lastName("Torres")
                .email("ana@test.com")
                .password("encoded")
                .phoneNumber("111222333")
                .role(Role.CUSTOMER)
                .build();
    }

    @Test
    @DisplayName("login con credenciales válidas debe retornar AuthResponse con token")
    void login_withValidCredentials_shouldReturnAuthResponse() {
        LoginRequest request = LoginRequest.builder()
                .email("ana@test.com")
                .password("password")
                .build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userService.findUserByEmail("ana@test.com")).thenReturn(user);
        when(userMapper.userToUserEntity(user)).thenReturn(userEntity);
        when(jwtService.getToken(userEntity)).thenReturn("test.jwt.token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("test.jwt.token", response.getToken());
        assertEquals(1L, response.getUserId());
        assertEquals("ana@test.com", response.getEmail());
        assertEquals("Ana", response.getName());
        assertEquals(Role.CUSTOMER, response.getRole());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    @DisplayName("register debe crear usuario, obtener token y retornar AuthResponse")
    void register_shouldCreateUserAndReturnAuthResponse() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Nuevo")
                .lastName("Usuario")
                .email("nuevo@test.com")
                .password("password123")
                .phoneNumber("999888777")
                .role(Role.SALESPERSON)
                .build();
        User savedUser = User.builder()
                .userId(2L)
                .name("Nuevo")
                .lastName("Usuario")
                .email("nuevo@test.com")
                .password("encoded")
                .phoneNumber("999888777")
                .role(Role.SALESPERSON)
                .build();
        UserEntity savedEntity = UserEntity.builder()
                .userId(2L)
                .name("Nuevo")
                .email("nuevo@test.com")
                .role(Role.SALESPERSON)
                .build();
        when(userService.saveUser(any())).thenReturn(savedUser);
        when(userMapper.userToUserEntity(savedUser)).thenReturn(savedEntity);
        when(jwtService.getToken(savedEntity)).thenReturn("nuevo.jwt.token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("nuevo.jwt.token", response.getToken());
        assertEquals(2L, response.getUserId());
        assertEquals("nuevo@test.com", response.getEmail());
        assertEquals(Role.SALESPERSON, response.getRole());
        verify(userService).saveUser(any());
    }

    @Test
    @DisplayName("googleLogin cuando el usuario ya existe debe retornar AuthResponse 200")
    void googleLogin_whenUserExists_shouldReturnAuthResponse() throws Exception {
        GoogleIdToken.Payload mockPayload = mock(GoogleIdToken.Payload.class);
        when(mockPayload.getEmail()).thenReturn("google@test.com");
        when(mockPayload.getOrDefault("given_name", "Usuario")).thenReturn("Google");
        when(mockPayload.getOrDefault("family_name", "Google")).thenReturn("User");

        GoogleIdToken mockIdToken = mock(GoogleIdToken.class);
        when(mockIdToken.getPayload()).thenReturn(mockPayload);

        GoogleIdTokenVerifier mockVerifier = mock(GoogleIdTokenVerifier.class);
        doReturn(mockIdToken).when(mockVerifier).verify(anyString());

        try (MockedConstruction<GoogleIdTokenVerifier.Builder> ignored =
                mockConstruction(GoogleIdTokenVerifier.Builder.class, (mock, ctx) -> {
                    when(mock.setAudience(anyList())).thenReturn(mock);
                    when(mock.build()).thenReturn(mockVerifier);
                })) {

            when(userService.findUserByEmail("google@test.com")).thenReturn(user);
            when(userMapper.userToUserEntity(user)).thenReturn(userEntity);
            when(jwtService.getToken(userEntity)).thenReturn("google.jwt.token");

            Object result = authService.googleLogin(
                    GoogleLoginRequest.builder().idToken("test.google.token").build());

            assertNotNull(result);
            assertInstanceOf(AuthResponse.class, result);
            assertEquals("google.jwt.token", ((AuthResponse) result).getToken());
        }
    }

    @Test
    @DisplayName("googleLogin cuando el usuario no existe debe retornar GooglePendingUserResponse 202")
    void googleLogin_whenUserNotFound_shouldReturnPendingResponse() throws Exception {
        GoogleIdToken.Payload mockPayload = mock(GoogleIdToken.Payload.class);
        when(mockPayload.getEmail()).thenReturn("nuevo@gmail.com");
        when(mockPayload.getOrDefault("given_name", "Usuario")).thenReturn("Nuevo");
        when(mockPayload.getOrDefault("family_name", "Google")).thenReturn("Usuario");

        GoogleIdToken mockIdToken = mock(GoogleIdToken.class);
        when(mockIdToken.getPayload()).thenReturn(mockPayload);

        GoogleIdTokenVerifier mockVerifier = mock(GoogleIdTokenVerifier.class);
        doReturn(mockIdToken).when(mockVerifier).verify(anyString());

        try (MockedConstruction<GoogleIdTokenVerifier.Builder> ignored =
                mockConstruction(GoogleIdTokenVerifier.Builder.class, (mock, ctx) -> {
                    when(mock.setAudience(anyList())).thenReturn(mock);
                    when(mock.build()).thenReturn(mockVerifier);
                })) {

            when(userService.findUserByEmail("nuevo@gmail.com"))
                    .thenThrow(new RuntimeException("Usuario no encontrado"));

            Object result = authService.googleLogin(
                    GoogleLoginRequest.builder().idToken("test.google.token").build());

            assertNotNull(result);
            assertInstanceOf(GooglePendingUserResponse.class, result);
            GooglePendingUserResponse pending = (GooglePendingUserResponse) result;
            assertEquals("nuevo@gmail.com", pending.getEmail());
            assertTrue(pending.isRequiresRoleSelection());
        }
    }

    @Test
    @DisplayName("googleRegister con nombre en el request debe usar el nombre del request")
    void googleRegister_withCustomName_shouldUseRequestName() throws Exception {
        GoogleIdToken.Payload mockPayload = mock(GoogleIdToken.Payload.class);
        when(mockPayload.getEmail()).thenReturn("reg@gmail.com");
        when(mockPayload.getOrDefault("given_name", "Usuario")).thenReturn("TokenName");
        when(mockPayload.getOrDefault("family_name", "Google")).thenReturn("TokenLast");

        GoogleIdToken mockIdToken = mock(GoogleIdToken.class);
        when(mockIdToken.getPayload()).thenReturn(mockPayload);

        GoogleIdTokenVerifier mockVerifier = mock(GoogleIdTokenVerifier.class);
        doReturn(mockIdToken).when(mockVerifier).verify(anyString());

        try (MockedConstruction<GoogleIdTokenVerifier.Builder> ignored =
                mockConstruction(GoogleIdTokenVerifier.Builder.class, (mock, ctx) -> {
                    when(mock.setAudience(anyList())).thenReturn(mock);
                    when(mock.build()).thenReturn(mockVerifier);
                })) {

            User savedUser = User.builder()
                    .userId(3L).name("CustomName").lastName("CustomLast")
                    .email("reg@gmail.com").role(Role.CUSTOMER).build();
            UserEntity savedEntity = UserEntity.builder()
                    .userId(3L).name("CustomName").email("reg@gmail.com").role(Role.CUSTOMER).build();

            when(userService.saveUser(any())).thenReturn(savedUser);
            when(userMapper.userToUserEntity(savedUser)).thenReturn(savedEntity);
            when(jwtService.getToken(savedEntity)).thenReturn("reg.jwt.token");

            GoogleRegisterRequest request = GoogleRegisterRequest.builder()
                    .idToken("test.google.token")
                    .name("CustomName")
                    .lastName("CustomLast")
                    .phoneNumber("123456789")
                    .role(Role.CUSTOMER)
                    .build();

            AuthResponse result = authService.googleRegister(request);

            assertNotNull(result);
            assertEquals("reg.jwt.token", result.getToken());
            verify(userService).saveUser(argThat(u ->
                    "CustomName".equals(u.getName()) && "CustomLast".equals(u.getLastName())));
        }
    }

    @Test
    @DisplayName("googleRegister sin nombre en el request debe usar nombre del token")
    void googleRegister_withNullName_shouldUseTokenName() throws Exception {
        GoogleIdToken.Payload mockPayload = mock(GoogleIdToken.Payload.class);
        when(mockPayload.getEmail()).thenReturn("reg2@gmail.com");
        when(mockPayload.getOrDefault("given_name", "Usuario")).thenReturn("TokenName");
        when(mockPayload.getOrDefault("family_name", "Google")).thenReturn("TokenLast");

        GoogleIdToken mockIdToken = mock(GoogleIdToken.class);
        when(mockIdToken.getPayload()).thenReturn(mockPayload);

        GoogleIdTokenVerifier mockVerifier = mock(GoogleIdTokenVerifier.class);
        doReturn(mockIdToken).when(mockVerifier).verify(anyString());

        try (MockedConstruction<GoogleIdTokenVerifier.Builder> ignored =
                mockConstruction(GoogleIdTokenVerifier.Builder.class, (mock, ctx) -> {
                    when(mock.setAudience(anyList())).thenReturn(mock);
                    when(mock.build()).thenReturn(mockVerifier);
                })) {

            User savedUser = User.builder()
                    .userId(4L).name("TokenName").lastName("TokenLast")
                    .email("reg2@gmail.com").role(Role.SALESPERSON).build();
            UserEntity savedEntity = UserEntity.builder()
                    .userId(4L).name("TokenName").email("reg2@gmail.com").role(Role.SALESPERSON).build();

            when(userService.saveUser(any())).thenReturn(savedUser);
            when(userMapper.userToUserEntity(savedUser)).thenReturn(savedEntity);
            when(jwtService.getToken(savedEntity)).thenReturn("reg2.jwt.token");

            GoogleRegisterRequest request = GoogleRegisterRequest.builder()
                    .idToken("test.google.token")
                    .name(null)
                    .lastName(null)
                    .phoneNumber("987654321")
                    .role(Role.SALESPERSON)
                    .build();

            AuthResponse result = authService.googleRegister(request);

            assertNotNull(result);
            assertEquals("reg2.jwt.token", result.getToken());
            verify(userService).saveUser(argThat(u ->
                    "TokenName".equals(u.getName()) && "TokenLast".equals(u.getLastName())));
        }
    }
}
