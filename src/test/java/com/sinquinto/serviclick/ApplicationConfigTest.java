package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Config.Infrastructure.ApplicationConfig;
import com.sinquinto.serviclick.User.Domain.Role;
import com.sinquinto.serviclick.User.Domain.User;
import com.sinquinto.serviclick.User.Domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationConfig config;

    @Test
    @DisplayName("passwordEncoder debe retornar BCryptPasswordEncoder funcional")
    void passwordEncoder_shouldReturnBCryptEncoder() {
        PasswordEncoder encoder = config.passwordEncoder();

        assertNotNull(encoder);
        assertInstanceOf(BCryptPasswordEncoder.class, encoder);
        assertTrue(encoder.matches("mypassword", encoder.encode("mypassword")));
    }

    @Test
    @DisplayName("userDetailsService cuando el usuario existe debe retornar UserDetails")
    void userDetailsService_whenUserExists_shouldReturnUserDetails() {
        User domainUser = User.builder()
                .userId(1L)
                .email("user@test.com")
                .password("encoded_password")
                .name("Test")
                .lastName("User")
                .role(Role.CUSTOMER)
                .registerDate(LocalDateTime.now())
                .build();
        when(userRepository.findByEmail("user@test.com")).thenReturn(domainUser);

        UserDetailsService uds = config.userDetailsService();
        UserDetails result = uds.loadUserByUsername("user@test.com");

        assertNotNull(result);
        assertEquals("user@test.com", result.getUsername());
        assertEquals("encoded_password", result.getPassword());
        assertFalse(result.getAuthorities().isEmpty());
    }

    @Test
    @DisplayName("userDetailsService cuando el usuario no existe debe lanzar UsernameNotFoundException")
    void userDetailsService_whenUserNotFound_shouldThrowUsernameNotFoundException() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(null);

        UserDetailsService uds = config.userDetailsService();

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> uds.loadUserByUsername("missing@test.com"));

        assertTrue(exception.getMessage().contains("missing@test.com"));
    }
}
