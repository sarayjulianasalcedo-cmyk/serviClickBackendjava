package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Config.Application.JwtService;
import com.sinquinto.serviclick.Config.Infrastructure.Filter.JwtAuthFilter;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock private JwtService jwtService;
    @Mock private UserDetailsService userDetailsService;
    @Mock private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter filter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Sin header Authorization debe pasar al siguiente filtro sin procesar JWT")
    void doFilter_withNoAuthHeader_shouldPassThrough() throws Exception {
        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).getUserNameFromToken(anyString());
    }

    @Test
    @DisplayName("Header Authorization sin prefijo Bearer debe pasar al siguiente filtro")
    void doFilter_withNonBearerHeader_shouldPassThrough() throws Exception {
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).getUserNameFromToken(anyString());
    }

    @Test
    @DisplayName("Bearer token válido con token válido debe setear el SecurityContext")
    void doFilter_withValidBearerAndValidToken_shouldSetAuthentication() throws Exception {
        request.addHeader("Authorization", "Bearer valid.jwt.token");

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("user@test.com").password("encoded").authorities("ROLE_USER").build();

        when(jwtService.getUserNameFromToken("valid.jwt.token")).thenReturn("user@test.com");
        when(userDetailsService.loadUserByUsername("user@test.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("valid.jwt.token", userDetails)).thenReturn(true);

        filter.doFilter(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("user@test.com",
                SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Bearer token con JWT inválido no debe setear el SecurityContext")
    void doFilter_withValidBearerButInvalidToken_shouldNotSetAuthentication() throws Exception {
        request.addHeader("Authorization", "Bearer invalid.jwt.token");

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("user@test.com").password("encoded").authorities("ROLE_USER").build();

        when(jwtService.getUserNameFromToken("invalid.jwt.token")).thenReturn("user@test.com");
        when(userDetailsService.loadUserByUsername("user@test.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("invalid.jwt.token", userDetails)).thenReturn(false);

        filter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Bearer token con email null no debe cargar UserDetails")
    void doFilter_whenEmailIsNull_shouldNotLoadUser() throws Exception {
        request.addHeader("Authorization", "Bearer some.token");
        when(jwtService.getUserNameFromToken("some.token")).thenReturn(null);

        filter.doFilter(request, response, filterChain);

        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(filterChain).doFilter(request, response);
    }
}
