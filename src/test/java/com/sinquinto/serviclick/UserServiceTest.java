package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Exception.Domain.ResourceDuplicateException;
import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import com.sinquinto.serviclick.User.Application.UserService;
import com.sinquinto.serviclick.User.Domain.Role;
import com.sinquinto.serviclick.User.Domain.User;
import com.sinquinto.serviclick.User.Domain.UserRepository;
import com.sinquinto.serviclick.User.Infrastructure.DTO.UserDTO;
import com.sinquinto.serviclick.User.Infrastructure.Mapper.UserDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository repository;
    @Mock private PasswordEncoder encoder;
    @Mock private UserDTOMapper mapper;

    @InjectMocks
    private UserService service;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(1L)
                .name("Carlos")
                .lastName("Gomez")
                .email("carlos@test.com")
                .password("rawPassword")
                .phoneNumber("123456789")
                .role(Role.CUSTOMER)
                .registerDate(LocalDateTime.now())
                .build();

        userDTO = new UserDTO(1L, "Carlos", "Gomez", "carlos@test.com", "123456789", Role.CUSTOMER);
    }

    @Test
    @DisplayName("saveUser con email nuevo debe cifrar contraseña y guardar")
    void saveUser_whenEmailNotExists_shouldEncodeAndSave() {
        when(repository.findByEmail("carlos@test.com")).thenReturn(null);
        when(encoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(repository.save(any())).thenReturn(user);

        User result = service.saveUser(user);

        assertNotNull(result);
        verify(encoder).encode("rawPassword");
        verify(repository).save(any());
    }

    @Test
    @DisplayName("saveUser con email duplicado debe lanzar ResourceDuplicateException")
    void saveUser_whenEmailExists_shouldThrow() {
        when(repository.findByEmail("carlos@test.com")).thenReturn(user);

        assertThrows(ResourceDuplicateException.class, () -> service.saveUser(user));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("findUserById con ID existente debe retornar el usuario")
    void findUserById_whenExists_shouldReturn() {
        when(repository.findById(1L)).thenReturn(user);

        User result = service.findUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
    }

    @Test
    @DisplayName("findUserById con ID inexistente debe lanzar ResourceNotFoundException")
    void findUserById_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.findUserById(99L));
    }

    @Test
    @DisplayName("findUserDTOById debe retornar DTO del usuario")
    void findUserDTOById_shouldReturnDTO() {
        when(repository.findById(1L)).thenReturn(user);
        when(mapper.userToUserDTO(user)).thenReturn(userDTO);

        UserDTO result = service.findUserDTOById(1L);

        assertNotNull(result);
        assertEquals("Carlos", result.name());
    }

    @Test
    @DisplayName("findUserByEmail con email existente debe retornar el usuario")
    void findUserByEmail_whenExists_shouldReturn() {
        when(repository.findByEmail("carlos@test.com")).thenReturn(user);

        User result = service.findUserByEmail("carlos@test.com");

        assertNotNull(result);
        assertEquals("carlos@test.com", result.getEmail());
    }

    @Test
    @DisplayName("findUserByEmail con email inexistente debe lanzar ResourceNotFoundException")
    void findUserByEmail_whenNotFound_shouldThrow() {
        when(repository.findByEmail("noexiste@test.com")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> service.findUserByEmail("noexiste@test.com"));
    }

    @Test
    @DisplayName("findAllUsers debe retornar lista de DTOs")
    void findAllUsers_shouldReturnDTOList() {
        when(repository.findAll()).thenReturn(List.of(user));
        when(mapper.userToUserDTO(user)).thenReturn(userDTO);

        List<UserDTO> result = service.findAllUsers();

        assertEquals(1, result.size());
        assertEquals("Carlos", result.get(0).name());
    }

    @Test
    @DisplayName("updateUser debe actualizar campos y guardar")
    void updateUser_shouldUpdateAndReturn() {
        User updateData = User.builder()
                .name("Carlos Actualizado")
                .lastName("Gomez")
                .email("carlosa@test.com")
                .phoneNumber("987654321")
                .role(Role.SALESPERSON)
                .password("")
                .build();
        when(repository.findById(1L)).thenReturn(user);
        when(repository.save(any())).thenReturn(user);

        User result = service.updateUser(1L, updateData);

        assertNotNull(result);
        verify(repository).save(any());
        verify(encoder, never()).encode(any());
    }

    @Test
    @DisplayName("updateUser con contraseña nueva debe cifrarla")
    void updateUser_withNewPassword_shouldEncodeIt() {
        User updateData = User.builder()
                .name("Carlos")
                .lastName("Gomez")
                .email("carlos@test.com")
                .phoneNumber("123")
                .role(Role.CUSTOMER)
                .password("newPassword")
                .build();
        when(repository.findById(1L)).thenReturn(user);
        when(encoder.encode("newPassword")).thenReturn("encodedNew");
        when(repository.save(any())).thenReturn(user);

        service.updateUser(1L, updateData);

        verify(encoder).encode("newPassword");
    }

    @Test
    @DisplayName("deleteUserById con ID existente debe eliminar")
    void deleteUserById_whenExists_shouldDelete() {
        when(repository.findById(1L)).thenReturn(user);

        service.deleteUserById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteUserById con ID inexistente debe lanzar excepción")
    void deleteUserById_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteUserById(99L));
    }

    @Test
    @DisplayName("countUsers debe retornar el conteo del repositorio")
    void countUsers_shouldReturnCount() {
        when(repository.countUsers()).thenReturn(7L);

        assertEquals(7L, service.countUsers());
    }
}