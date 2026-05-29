package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Appointment.Application.AppointmentService;
import com.sinquinto.serviclick.Appointment.Domain.Appointment;
import com.sinquinto.serviclick.Appointment.Domain.AppointmentRepository;
import com.sinquinto.serviclick.Appointment.Domain.AppointmentStatus;
import com.sinquinto.serviclick.Appointment.Infrastructure.DTO.AppointmentCustomerDTO;
import com.sinquinto.serviclick.Appointment.Infrastructure.DTO.AppointmentSellerDTO;
import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import com.sinquinto.serviclick.Notification.Application.NotificationService;
import com.sinquinto.serviclick.Notification.Domain.Notification;
import com.sinquinto.serviclick.Rating.Application.RatingService;
import com.sinquinto.serviclick.ServiceOffer.Application.ServiceOfferService;
import com.sinquinto.serviclick.ServiceOffer.Domain.ServiceOffer;
import com.sinquinto.serviclick.User.Application.UserService;
import com.sinquinto.serviclick.User.Domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock private AppointmentRepository repository;
    @Mock private ServiceOfferService serviceOfferService;
    @Mock private UserService userService;
    @Mock private RatingService ratingService;
    @Mock private NotificationService notificationService;

    @InjectMocks
    private AppointmentService service;

    private Appointment appointment;
    private ServiceOffer serviceOffer;
    private User customer;
    private User seller;

    @BeforeEach
    void setUp() {
        appointment = Appointment.builder()
                .appointmentId(1L)
                .userId(10L)
                .serviceOfferId(20L)
                .date(LocalDateTime.now())
                .status(AppointmentStatus.PENDING)
                .build();

        serviceOffer = ServiceOffer.builder()
                .serviceOfferId(20L)
                .sellerId(30L)
                .title("Servicio Test")
                .category("Hogar")
                .price(BigDecimal.valueOf(50.0))
                .build();

        customer = User.builder().userId(10L).name("Juan").lastName("Perez").email("juan@test.com").build();
        seller = User.builder().userId(30L).name("Maria").lastName("Lopez").email("maria@test.com").build();
    }

    @Test
    @DisplayName("saveAppointment debe asignar estado PENDING y crear notificación al vendedor")
    void saveAppointment_shouldSetPendingAndCreateNotification() {
        when(repository.save(any())).thenReturn(appointment);
        when(serviceOfferService.findServiceOfferById(20L)).thenReturn(serviceOffer);
        when(userService.findUserById(10L)).thenReturn(customer);
        when(notificationService.createNotification(any())).thenReturn(Notification.builder().id(1L).build());

        Appointment result = service.saveAppointment(appointment);

        assertNotNull(result);
        assertEquals(AppointmentStatus.PENDING, result.getStatus());
        verify(repository).save(any());
        verify(notificationService).createNotification(any());
    }

    @Test
    @DisplayName("saveAppointment sigue funcionando aunque la notificación falle")
    void saveAppointment_whenNotificationFails_shouldStillReturn() {
        when(repository.save(any())).thenReturn(appointment);
        when(serviceOfferService.findServiceOfferById(20L)).thenThrow(new RuntimeException("error de notif"));

        Appointment result = service.saveAppointment(appointment);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("saveAppointment con notificación duplicada (null) sigue funcionando")
    void saveAppointment_whenNotificationReturnNull_shouldStillReturn() {
        when(repository.save(any())).thenReturn(appointment);
        when(serviceOfferService.findServiceOfferById(20L)).thenReturn(serviceOffer);
        when(userService.findUserById(10L)).thenReturn(customer);
        when(notificationService.createNotification(any())).thenReturn(null);

        Appointment result = service.saveAppointment(appointment);

        assertNotNull(result);
    }

    @Test
    @DisplayName("findAppointmentById con ID existente debe retornar el appointment")
    void findAppointmentById_whenExists_shouldReturn() {
        when(repository.findById(1L)).thenReturn(appointment);

        Appointment result = service.findAppointmentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getAppointmentId());
    }

    @Test
    @DisplayName("findAppointmentById con ID inexistente debe lanzar ResourceNotFoundException")
    void findAppointmentById_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.findAppointmentById(99L));
    }

    @Test
    @DisplayName("findAllAppointments debe retornar lista completa")
    void findAllAppointments_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(appointment));

        List<Appointment> result = service.findAllAppointments();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findAppointmentsByUser debe retornar appointments del usuario")
    void findAppointmentsByUser_shouldReturnList() {
        when(repository.findByUserId(10L)).thenReturn(List.of(appointment));

        List<Appointment> result = service.findAppointmentsByUser(10L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findAppointmentsByServiceOffer debe retornar appointments del servicio")
    void findAppointmentsByServiceOffer_shouldReturnList() {
        when(repository.findByServiceOfferId(20L)).thenReturn(List.of(appointment));

        List<Appointment> result = service.findAppointmentsByServiceOffer(20L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("updateAppointment con ID existente debe actualizar y retornar")
    void updateAppointment_whenExists_shouldUpdateAndReturn() {
        when(repository.findById(1L)).thenReturn(appointment);
        when(repository.save(any())).thenReturn(appointment);
        Appointment updateData = Appointment.builder()
                .date(LocalDateTime.now().plusDays(1))
                .status(AppointmentStatus.ACCEPTED)
                .build();

        Appointment result = service.updateAppointment(1L, updateData);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("updateAppointment con ID inexistente debe lanzar excepción")
    void updateAppointment_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateAppointment(99L, appointment));
    }

    @Test
    @DisplayName("deleteAppointmentById con ID existente debe eliminar")
    void deleteAppointmentById_whenExists_shouldDelete() {
        when(repository.findById(1L)).thenReturn(appointment);

        service.deleteAppointmentById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteAppointmentById con ID inexistente debe lanzar excepción")
    void deleteAppointmentById_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteAppointmentById(99L));
    }

    @Test
    @DisplayName("countAppointments debe retornar el conteo del repositorio")
    void countAppointments_shouldReturnCount() {
        when(repository.countAppointments()).thenReturn(5L);

        assertEquals(5L, service.countAppointments());
    }

    @Test
    @DisplayName("updateAppointmentStatus a ACCEPTED debe crear notificación ACCEPTED")
    void updateAppointmentStatus_toAccepted_shouldCreateAcceptedNotif() {
        when(repository.findById(1L)).thenReturn(appointment);
        when(repository.save(any())).thenReturn(appointment);
        when(serviceOfferService.findServiceOfferById(20L)).thenReturn(serviceOffer);
        when(userService.findUserById(30L)).thenReturn(seller);
        when(notificationService.createNotification(any())).thenReturn(Notification.builder().id(2L).build());

        Appointment result = service.updateAppointmentStatus(1L, AppointmentStatus.ACCEPTED);

        assertNotNull(result);
        verify(notificationService).createNotification(argThat(n ->
                n.getType().name().equals("APPOINTMENT_ACCEPTED")));
    }

    @Test
    @DisplayName("updateAppointmentStatus a REJECTED debe crear notificación REJECTED")
    void updateAppointmentStatus_toRejected_shouldCreateRejectedNotif() {
        when(repository.findById(1L)).thenReturn(appointment);
        when(repository.save(any())).thenReturn(appointment);
        when(serviceOfferService.findServiceOfferById(20L)).thenReturn(serviceOffer);
        when(userService.findUserById(30L)).thenReturn(seller);
        when(notificationService.createNotification(any())).thenReturn(null);

        service.updateAppointmentStatus(1L, AppointmentStatus.REJECTED);

        verify(notificationService).createNotification(argThat(n ->
                n.getType().name().equals("APPOINTMENT_REJECTED")));
    }

    @Test
    @DisplayName("updateAppointmentStatus a COMPLETED debe crear notificación COMPLETED")
    void updateAppointmentStatus_toCompleted_shouldCreateCompletedNotif() {
        when(repository.findById(1L)).thenReturn(appointment);
        when(repository.save(any())).thenReturn(appointment);
        when(serviceOfferService.findServiceOfferById(20L)).thenReturn(serviceOffer);
        when(userService.findUserById(30L)).thenReturn(seller);
        when(notificationService.createNotification(any())).thenReturn(Notification.builder().id(3L).build());

        service.updateAppointmentStatus(1L, AppointmentStatus.COMPLETED);

        verify(notificationService).createNotification(argThat(n ->
                n.getType().name().equals("APPOINTMENT_COMPLETED")));
    }

    @Test
    @DisplayName("updateAppointmentStatus a PENDING no crea notificación")
    void updateAppointmentStatus_toPending_shouldNotCreateNotif() {
        when(repository.findById(1L)).thenReturn(appointment);
        when(repository.save(any())).thenReturn(appointment);
        when(serviceOfferService.findServiceOfferById(20L)).thenReturn(serviceOffer);
        when(userService.findUserById(30L)).thenReturn(seller);

        service.updateAppointmentStatus(1L, AppointmentStatus.PENDING);

        verify(notificationService, never()).createNotification(any());
    }

    @Test
    @DisplayName("updateAppointmentStatus con ID inexistente debe lanzar excepción")
    void updateAppointmentStatus_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateAppointmentStatus(99L, AppointmentStatus.ACCEPTED));
    }

    @Test
    @DisplayName("updateAppointmentStatus sigue funcionando aunque falle la notificación")
    void updateAppointmentStatus_whenNotifFails_shouldStillReturn() {
        when(repository.findById(1L)).thenReturn(appointment);
        when(repository.save(any())).thenReturn(appointment);
        when(serviceOfferService.findServiceOfferById(20L)).thenThrow(new RuntimeException("fallo"));

        Appointment result = service.updateAppointmentStatus(1L, AppointmentStatus.ACCEPTED);

        assertNotNull(result);
    }

    @Test
    @DisplayName("findAppointmentDetailsByUser debe retornar DTOs del cliente")
    void findAppointmentDetailsByUser_shouldReturnDTOs() {
        when(repository.findByUserId(10L)).thenReturn(List.of(appointment));
        when(serviceOfferService.findServiceOfferById(20L)).thenReturn(serviceOffer);
        when(userService.findUserById(30L)).thenReturn(seller);
        when(ratingService.existsRatingForAppointment(1L)).thenReturn(false);

        List<AppointmentCustomerDTO> result = service.findAppointmentDetailsByUser(10L);

        assertEquals(1, result.size());
        assertEquals("Servicio Test", result.get(0).serviceTitle());
        assertFalse(result.get(0).hasRating());
    }

    @Test
    @DisplayName("findHistorialByUser debe retornar appointments COMPLETED del cliente")
    void findHistorialByUser_shouldReturnCompletedAppointments() {
        Appointment completedApp = Appointment.builder()
                .appointmentId(2L).userId(10L).serviceOfferId(20L)
                .status(AppointmentStatus.COMPLETED).build();
        when(repository.findByUserIdAndStatus(10L, AppointmentStatus.COMPLETED))
                .thenReturn(List.of(completedApp));
        when(serviceOfferService.findServiceOfferById(20L)).thenReturn(serviceOffer);
        when(userService.findUserById(30L)).thenReturn(seller);
        when(ratingService.existsRatingForAppointment(2L)).thenReturn(true);

        List<AppointmentCustomerDTO> result = service.findHistorialByUser(10L);

        assertEquals(1, result.size());
        assertTrue(result.get(0).hasRating());
    }

    @Test
    @DisplayName("findAppointmentsForSeller sin ofertas debe retornar lista vacía")
    void findAppointmentsForSeller_whenNoOffers_shouldReturnEmpty() {
        when(serviceOfferService.findServiceOffersBySeller(30L)).thenReturn(List.of());

        List<AppointmentSellerDTO> result = service.findAppointmentsForSeller(30L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findAppointmentsForSeller con ofertas debe retornar DTOs del vendedor")
    void findAppointmentsForSeller_withOffers_shouldReturnDTOs() {
        when(serviceOfferService.findServiceOffersBySeller(30L)).thenReturn(List.of(serviceOffer));
        when(repository.findByServiceOfferIdIn(anyList())).thenReturn(List.of(appointment));
        when(userService.findUserById(10L)).thenReturn(customer);

        List<AppointmentSellerDTO> result = service.findAppointmentsForSeller(30L);

        assertEquals(1, result.size());
        assertEquals("Servicio Test", result.get(0).serviceTitle());
        assertEquals("Juan Perez", result.get(0).customerName());
    }
}