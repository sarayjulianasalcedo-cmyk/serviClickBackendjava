package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Config.Application.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        ReflectionTestUtils.setField(jwtService, "secretKey", base64Key);

        userDetails = User.withUsername("test@example.com")
                .password("password")
                .authorities("CUSTOMER")
                .build();
    }

    @Test
    @DisplayName("getToken debe devolver token no nulo")
    void getToken_shouldReturnNonNullToken() {
        String token = jwtService.getToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("getUserNameFromToken debe devolver el email del usuario")
    void getUserNameFromToken_shouldReturnUsername() {
        String token = jwtService.getToken(userDetails);
        String username = jwtService.getUserNameFromToken(token);
        assertEquals("test@example.com", username);
    }

    @Test
    @DisplayName("isTokenValid con token válido debe retornar true")
    void isTokenValid_withValidToken_shouldReturnTrue() {
        String token = jwtService.getToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    @DisplayName("isTokenValid con usuario distinto debe retornar false")
    void isTokenValid_withWrongUser_shouldReturnFalse() {
        String token = jwtService.getToken(userDetails);
        UserDetails otherUser = User.withUsername("other@example.com")
                .password("password")
                .authorities("CUSTOMER")
                .build();
        assertFalse(jwtService.isTokenValid(token, otherUser));
    }

    @Test
    @DisplayName("getExpirationDate debe devolver fecha futura")
    void getExpirationDate_shouldReturnFutureDate() {
        String token = jwtService.getToken(userDetails);
        Date expDate = jwtService.getExpirationDate(token);
        assertNotNull(expDate);
        assertTrue(expDate.after(new Date()));
    }

    @Test
    @DisplayName("getToken con claims extra debe incluirlos en el token")
    void getToken_withExtraClaims_shouldIncludeClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("customClaim", "testValue");
        String token = jwtService.getToken(claims, userDetails);
        assertNotNull(token);
        String claimValue = jwtService.getClaim(token, c -> c.get("customClaim", String.class));
        assertEquals("testValue", claimValue);
    }

    @Test
    @DisplayName("getKey debe devolver una clave no nula")
    void getKey_shouldReturnKey() {
        assertNotNull(jwtService.getKey());
    }
}