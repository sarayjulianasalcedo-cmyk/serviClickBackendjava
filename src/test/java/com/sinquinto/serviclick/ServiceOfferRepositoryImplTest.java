package com.sinquinto.serviclick;

import com.sinquinto.serviclick.ServiceOffer.Domain.ServiceOffer;
import com.sinquinto.serviclick.ServiceOffer.Infrastructure.Entity.ServiceOfferEntity;
import com.sinquinto.serviclick.ServiceOffer.Infrastructure.Mapper.ServiceOfferMapper;
import com.sinquinto.serviclick.ServiceOffer.Infrastructure.Mapper.ServiceOfferMapperImpl;
import com.sinquinto.serviclick.ServiceOffer.Infrastructure.Repository.ServiceOfferJpaRepository;
import com.sinquinto.serviclick.ServiceOffer.Infrastructure.Repository.ServiceOfferRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceOfferRepositoryImplTest {

    @Mock
    private ServiceOfferJpaRepository jpaRepository;

    private ServiceOfferMapper mapper;
    private ServiceOfferRepositoryImpl repository;

    private ServiceOfferEntity entity;
    private ServiceOffer domain;

    @BeforeEach
    void setUp() {
        mapper = new ServiceOfferMapperImpl();
        repository = new ServiceOfferRepositoryImpl(jpaRepository, mapper);

        entity = ServiceOfferEntity.builder()
                .serviceOfferId(1L)
                .sellerId(10L)
                .title("Pintura")
                .description("Pintamos tu casa")
                .price(BigDecimal.valueOf(100.0))
                .estimatedDuration(3)
                .category("Hogar")
                .publicationDate(LocalDateTime.now())
                .build();

        domain = ServiceOffer.builder()
                .serviceOfferId(1L)
                .sellerId(10L)
                .title("Pintura")
                .description("Pintamos tu casa")
                .price(BigDecimal.valueOf(100.0))
                .estimatedDuration(3)
                .category("Hogar")
                .publicationDate(entity.getPublicationDate())
                .build();
    }

    @Test
    @DisplayName("save debe convertir, guardar y retornar dominio")
    void save_shouldConvertAndSave() {
        when(jpaRepository.save(any())).thenReturn(entity);

        ServiceOffer result = repository.save(domain);

        assertNotNull(result);
        assertEquals(1L, result.getServiceOfferId());
        assertEquals("Pintura", result.getTitle());
        verify(jpaRepository).save(any());
    }

    @Test
    @DisplayName("findById con ID existente debe retornar dominio")
    void findById_whenExists_shouldReturn() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        ServiceOffer result = repository.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getServiceOfferId());
    }

    @Test
    @DisplayName("findById con ID inexistente debe retornar null")
    void findById_whenNotFound_shouldReturnNull() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        ServiceOffer result = repository.findById(99L);

        assertNull(result);
    }

    @Test
    @DisplayName("findAll debe retornar lista mapeada")
    void findAll_shouldReturnMappedList() {
        when(jpaRepository.findAll()).thenReturn(List.of(entity));

        List<ServiceOffer> result = repository.findAll();

        assertEquals(1, result.size());
        assertEquals("Pintura", result.get(0).getTitle());
    }

    @Test
    @DisplayName("findBySellerId debe retornar lista mapeada")
    void findBySellerId_shouldReturnMappedList() {
        when(jpaRepository.findBySellerId(10L)).thenReturn(List.of(entity));

        List<ServiceOffer> result = repository.findBySellerId(10L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("deleteById debe delegar al jpaRepository")
    void deleteById_shouldDelegate() {
        repository.deleteById(1L);
        verify(jpaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("countServiceOffers debe retornar el count del jpaRepository")
    void countServiceOffers_shouldReturnCount() {
        when(jpaRepository.count()).thenReturn(12L);

        assertEquals(12L, repository.countServiceOffers());
    }
}