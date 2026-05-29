package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import com.sinquinto.serviclick.ServiceOffer.Application.ServiceOfferService;
import com.sinquinto.serviclick.ServiceOffer.Domain.ServiceOffer;
import com.sinquinto.serviclick.ServiceOffer.Domain.ServiceOfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceOfferServiceTest {

    @Mock
    private ServiceOfferRepository repository;

    @InjectMocks
    private ServiceOfferService service;

    private ServiceOffer serviceOffer;

    @BeforeEach
    void setUp() {
        serviceOffer = ServiceOffer.builder()
                .serviceOfferId(1L)
                .sellerId(10L)
                .title("Pintura de interiores")
                .description("Pintamos tu casa")
                .price(BigDecimal.valueOf(100.0))
                .estimatedDuration(3)
                .category("Hogar")
                .build();
    }

    @Test
    @DisplayName("saveServiceOffer debe asignar fecha de publicación y guardar")
    void saveServiceOffer_shouldSetPublicationDateAndSave() {
        when(repository.save(any())).thenReturn(serviceOffer);

        ServiceOffer result = service.saveServiceOffer(serviceOffer);

        assertNotNull(result);
        assertNotNull(serviceOffer.getPublicationDate());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("findServiceOfferById con ID existente debe retornar el servicio")
    void findServiceOfferById_whenExists_shouldReturn() {
        when(repository.findById(1L)).thenReturn(serviceOffer);

        ServiceOffer result = service.findServiceOfferById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getServiceOfferId());
    }

    @Test
    @DisplayName("findServiceOfferById con ID inexistente debe lanzar ResourceNotFoundException")
    void findServiceOfferById_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.findServiceOfferById(99L));
    }

    @Test
    @DisplayName("findAllServiceOffers debe retornar lista completa")
    void findAllServiceOffers_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(serviceOffer));

        List<ServiceOffer> result = service.findAllServiceOffers();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findServiceOffersBySeller debe retornar los servicios del vendedor")
    void findServiceOffersBySeller_shouldReturnList() {
        when(repository.findBySellerId(10L)).thenReturn(List.of(serviceOffer));

        List<ServiceOffer> result = service.findServiceOffersBySeller(10L);

        assertEquals(1, result.size());
        assertEquals("Pintura de interiores", result.get(0).getTitle());
    }

    @Test
    @DisplayName("findServiceOffersBySeller con vendedor sin servicios debe retornar lista vacía")
    void findServiceOffersBySeller_whenNoServices_shouldReturnEmptyList() {
        when(repository.findBySellerId(99L)).thenReturn(List.of());

        List<ServiceOffer> result = service.findServiceOffersBySeller(99L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findServiceOffersBySeller no debe retornar servicios de otros vendedores")
    void findServiceOffersBySeller_shouldNotReturnOtherSellerServices() {
        ServiceOffer otherSellerOffer = ServiceOffer.builder()
                .serviceOfferId(2L).sellerId(20L)
                .title("Servicio de otro vendedor")
                .build();
        when(repository.findBySellerId(10L)).thenReturn(List.of(serviceOffer));
        when(repository.findBySellerId(20L)).thenReturn(List.of(otherSellerOffer));

        List<ServiceOffer> resultSeller10 = service.findServiceOffersBySeller(10L);
        List<ServiceOffer> resultSeller20 = service.findServiceOffersBySeller(20L);

        assertEquals(1, resultSeller10.size());
        assertEquals(10L, resultSeller10.get(0).getSellerId());
        assertEquals(1, resultSeller20.size());
        assertEquals(20L, resultSeller20.get(0).getSellerId());
    }

    @Test
    @DisplayName("updateServiceOffer con ID existente debe actualizar campos y retornar")
    void updateServiceOffer_whenExists_shouldUpdateAndReturn() {
        ServiceOffer updateData = ServiceOffer.builder()
                .title("Pintura exterior")
                .description("Nueva desc")
                .price(BigDecimal.valueOf(150.0))
                .estimatedDuration(5)
                .category("Construcción")
                .photo("foto.jpg")
                .build();
        when(repository.findById(1L)).thenReturn(serviceOffer);
        when(repository.save(any())).thenReturn(serviceOffer);

        ServiceOffer result = service.updateServiceOffer(1L, updateData);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("updateServiceOffer con ID inexistente debe lanzar excepción")
    void updateServiceOffer_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateServiceOffer(99L, serviceOffer));
    }

    @Test
    @DisplayName("deleteServiceOfferById con ID existente debe eliminar")
    void deleteServiceOfferById_whenExists_shouldDelete() {
        when(repository.findById(1L)).thenReturn(serviceOffer);

        service.deleteServiceOfferById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteServiceOfferById con ID inexistente debe lanzar excepción")
    void deleteServiceOfferById_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteServiceOfferById(99L));
    }

    @Test
    @DisplayName("countServiceOffers debe retornar el conteo del repositorio")
    void countServiceOffers_shouldReturnCount() {
        when(repository.countServiceOffers()).thenReturn(15L);

        assertEquals(15L, service.countServiceOffers());
    }
}