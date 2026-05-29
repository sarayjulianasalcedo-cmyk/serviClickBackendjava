package com.sinquinto.serviclick;

import com.sinquinto.serviclick.User.Domain.Role;
import com.sinquinto.serviclick.User.Domain.User;
import com.sinquinto.serviclick.User.Infrastructure.Entity.UserEntity;
import com.sinquinto.serviclick.User.Infrastructure.Mapper.UserMapper;
import com.sinquinto.serviclick.User.Infrastructure.Mapper.UserMapperImpl;
import com.sinquinto.serviclick.User.Infrastructure.Repository.SpringUserRepository;
import com.sinquinto.serviclick.User.Infrastructure.Repository.UserMySqlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserMySqlRepositoryTest {

    @Mock
    private SpringUserRepository springRepo;

    private UserMapper mapper;
    private UserMySqlRepository repository;

    private UserEntity entity;
    private User domain;

    @BeforeEach
    void setUp() {
        mapper = new UserMapperImpl();
        repository = new UserMySqlRepository(springRepo, mapper);

        entity = UserEntity.builder()
                .userId(1L)
                .name("Carlos")
                .lastName("Gomez")
                .email("carlos@test.com")
                .password("encoded")
                .phoneNumber("123456789")
                .role(Role.CUSTOMER)
                .build();

        domain = User.builder()
                .userId(1L)
                .name("Carlos")
                .lastName("Gomez")
                .email("carlos@test.com")
                .password("encoded")
                .phoneNumber("123456789")
                .role(Role.CUSTOMER)
                .build();
    }

    @Test
    @DisplayName("save debe convertir, guardar y retornar dominio")
    void save_shouldConvertAndSave() {
        when(springRepo.save(any())).thenReturn(entity);

        User result = repository.save(domain);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("Carlos", result.getName());
        verify(springRepo).save(any());
    }

    @Test
    @DisplayName("findByEmail con email existente debe retornar dominio")
    void findByEmail_whenExists_shouldReturn() {
        when(springRepo.findByEmail("carlos@test.com")).thenReturn(entity);

        User result = repository.findByEmail("carlos@test.com");

        assertNotNull(result);
        assertEquals("carlos@test.com", result.getEmail());
    }

    @Test
    @DisplayName("findByEmail con email inexistente debe retornar null")
    void findByEmail_whenNotFound_shouldReturnNull() {
        when(springRepo.findByEmail("noexiste@test.com")).thenReturn(null);

        User result = repository.findByEmail("noexiste@test.com");

        assertNull(result);
    }

    @Test
    @DisplayName("findById con ID existente debe retornar dominio")
    void findById_whenExists_shouldReturn() {
        when(springRepo.findById(1L)).thenReturn(Optional.of(entity));

        User result = repository.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
    }

    @Test
    @DisplayName("findById con ID inexistente debe retornar null")
    void findById_whenNotFound_shouldReturnNull() {
        when(springRepo.findById(99L)).thenReturn(Optional.empty());

        User result = repository.findById(99L);

        assertNull(result);
    }

    @Test
    @DisplayName("findAll debe retornar lista de dominios mapeada")
    void findAll_shouldReturnMappedList() {
        when(springRepo.findAll()).thenReturn(List.of(entity));

        List<User> result = repository.findAll();

        assertEquals(1, result.size());
        assertEquals("Carlos", result.get(0).getName());
    }

    @Test
    @DisplayName("deleteById debe delegar al springRepo")
    void deleteById_shouldDelegate() {
        repository.deleteById(1L);
        verify(springRepo).deleteById(1L);
    }

    @Test
    @DisplayName("countUsers debe retornar el count del springRepo")
    void countUsers_shouldReturnCount() {
        when(springRepo.count()).thenReturn(10L);

        assertEquals(10L, repository.countUsers());
    }
}