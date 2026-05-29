package com.sinquinto.serviclick.Appointment.Application;

import com.sinquinto.serviclick.Appointment.Application.UseCases.AppointmentCountUseCase;
import com.sinquinto.serviclick.Appointment.Application.UseCases.AppointmentCrudUseCase;
import com.sinquinto.serviclick.Appointment.Domain.Appointment;
import com.sinquinto.serviclick.Appointment.Domain.AppointmentRepository;
import com.sinquinto.serviclick.Appointment.Domain.AppointmentStatus;
import com.sinquinto.serviclick.Appointment.Infrastructure.DTO.AppointmentCustomerDTO;
import com.sinquinto.serviclick.Appointment.Infrastructure.DTO.AppointmentSellerDTO;
import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import com.sinquinto.serviclick.Notification.Application.NotificationService;
import com.sinquinto.serviclick.Notification.Domain.Notification;
import com.sinquinto.serviclick.Notification.Domain.NotificationType;
import com.sinquinto.serviclick.Rating.Application.RatingService;
import com.sinquinto.serviclick.ServiceOffer.Application.ServiceOfferService;
import com.sinquinto.serviclick.ServiceOffer.Domain.ServiceOffer;
import com.sinquinto.serviclick.User.Application.UserService;
import com.sinquinto.serviclick.User.Domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppointmentService implements AppointmentCrudUseCase, AppointmentCountUseCase {

    private final AppointmentRepository repository;
    private final ServiceOfferService serviceOfferService;
    private final UserService userService;
    private final RatingService ratingService;
    private final NotificationService notificationService;

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        appointment.setStatus(AppointmentStatus.PENDING);
        Appointment saved = repository.save(appointment);

        log.debug("[NOTIF] Appointment creado: id={}, userId={}, serviceOfferId={}",
                saved.getAppointmentId(), saved.getUserId(), saved.getServiceOfferId());

        try {
            ServiceOffer serviceOffer = serviceOfferService.findServiceOfferById(saved.getServiceOfferId());
            log.debug("[NOTIF] ServiceOffer encontrado: id={}, sellerId={}, titulo='{}'",
                    serviceOffer.getServiceOfferId(), serviceOffer.getSellerId(), serviceOffer.getTitle());

            User customer = userService.findUserById(saved.getUserId());
            String customerName = customer.getName() + " " + customer.getLastName();
            log.debug("[NOTIF] Cliente: id={}, nombre='{}'", saved.getUserId(), customerName);

            Notification created = notificationService.createNotification(Notification.builder()
                    .userId(serviceOffer.getSellerId())
                    .type(NotificationType.APPOINTMENT_REQUESTED)
                    .title("Nueva solicitud de servicio")
                    .message(customerName + " quiere solicitar tu servicio: " + serviceOffer.getTitle())
                    .appointmentId(saved.getAppointmentId())
                    .serviceOfferId(saved.getServiceOfferId())
                    .build());

            if (created != null) {
                log.debug("[NOTIF] Notificación APPOINTMENT_REQUESTED creada: id={} para sellerId={}",
                        created.getId(), serviceOffer.getSellerId());
            } else {
                log.debug("[NOTIF] Notificación APPOINTMENT_REQUESTED omitida (duplicada) para appointmentId={}",
                        saved.getAppointmentId());
            }
        } catch (Exception e) {
            log.error("[NOTIF] Error al crear notificación para appointmentId={}: {}",
                    saved.getAppointmentId(), e.getMessage(), e);
        }

        return saved;
    }

    @Override
    public Appointment findAppointmentById(Long id) {
        Appointment appointment = repository.findById(id);
        if (appointment == null) {
            throw new ResourceNotFoundException("Appointment with id " + id + " not found");
        }
        return appointment;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Appointment> findAllAppointments() {
        return repository.findAll();
    }

    @Override
    public List<Appointment> findAppointmentsByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<Appointment> findAppointmentsByServiceOffer(Long serviceOfferId) {
        return repository.findByServiceOfferId(serviceOfferId);
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment appointment) {
        Appointment appointmentDB = repository.findById(id);
        if (appointmentDB == null) {
            throw new ResourceNotFoundException("Appointment with id " + id + " not found");
        }
        appointmentDB.setDate(appointment.getDate());
        appointmentDB.setStatus(appointment.getStatus());
        return repository.save(appointmentDB);
    }

    @Override
    public void deleteAppointmentById(Long id) {
        Appointment appointmentDB = repository.findById(id);
        if (appointmentDB == null) {
            throw new ResourceNotFoundException("Appointment with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override
    public Long countAppointments() {
        return repository.countAppointments();
    }

    public List<AppointmentCustomerDTO> findAppointmentDetailsByUser(Long userId) {
        List<Appointment> appointments = repository.findByUserId(userId);
        return appointments.stream().map(a -> toCustomerDTO(a)).toList();
    }

    public List<AppointmentCustomerDTO> findHistorialByUser(Long userId) {
        List<Appointment> appointments = repository.findByUserIdAndStatus(userId, AppointmentStatus.COMPLETED);
        return appointments.stream().map(a -> toCustomerDTO(a)).toList();
    }

    private AppointmentCustomerDTO toCustomerDTO(Appointment appointment) {
        ServiceOffer serviceOffer = serviceOfferService.findServiceOfferById(appointment.getServiceOfferId());
        User seller = userService.findUserById(serviceOffer.getSellerId());
        boolean hasRating = ratingService.existsRatingForAppointment(appointment.getAppointmentId());
        return new AppointmentCustomerDTO(
                appointment.getAppointmentId(),
                appointment.getServiceOfferId(),
                serviceOffer.getTitle(),
                seller.getName() + " " + seller.getLastName(),
                serviceOffer.getCategory(),
                appointment.getDate(),
                serviceOffer.getPrice(),
                appointment.getStatus(),
                hasRating
        );
    }

    public List<AppointmentSellerDTO> findAppointmentsForSeller(Long sellerId) {
        List<ServiceOffer> sellerOffers = serviceOfferService.findServiceOffersBySeller(sellerId);
        if (sellerOffers.isEmpty()) return List.of();

        List<Long> serviceOfferIds = sellerOffers.stream()
                .map(ServiceOffer::getServiceOfferId)
                .toList();

        Map<Long, ServiceOffer> offerMap = sellerOffers.stream()
                .collect(Collectors.toMap(ServiceOffer::getServiceOfferId, so -> so));

        List<Appointment> appointments = repository.findByServiceOfferIdIn(serviceOfferIds);
        return appointments.stream().map(appointment -> {
            ServiceOffer serviceOffer = offerMap.get(appointment.getServiceOfferId());
            User customer = userService.findUserById(appointment.getUserId());
            return new AppointmentSellerDTO(
                    appointment.getAppointmentId(),
                    appointment.getServiceOfferId(),
                    serviceOffer.getTitle(),
                    customer.getName() + " " + customer.getLastName(),
                    customer.getEmail(),
                    appointment.getDate(),
                    serviceOffer.getPrice(),
                    appointment.getStatus()
            );
        }).toList();
    }

    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = repository.findById(id);
        if (appointment == null) {
            throw new ResourceNotFoundException("Appointment with id " + id + " not found");
        }
        appointment.setStatus(status);
        Appointment updated = repository.save(appointment);

        log.debug("[NOTIF] Status actualizado: appointmentId={}, nuevo status={}", id, status);

        try {
            ServiceOffer serviceOffer = serviceOfferService.findServiceOfferById(updated.getServiceOfferId());
            User seller = userService.findUserById(serviceOffer.getSellerId());
            String sellerName = seller.getName() + " " + seller.getLastName();

            Notification notif = null;
            if (status == AppointmentStatus.ACCEPTED) {
                notif = notificationService.createNotification(Notification.builder()
                        .userId(updated.getUserId())
                        .type(NotificationType.APPOINTMENT_ACCEPTED)
                        .title("Solicitud aceptada")
                        .message(sellerName + " aceptó tu solicitud: " + serviceOffer.getTitle())
                        .appointmentId(updated.getAppointmentId())
                        .serviceOfferId(updated.getServiceOfferId())
                        .build());
            } else if (status == AppointmentStatus.REJECTED) {
                notif = notificationService.createNotification(Notification.builder()
                        .userId(updated.getUserId())
                        .type(NotificationType.APPOINTMENT_REJECTED)
                        .title("Solicitud rechazada")
                        .message(sellerName + " rechazó tu solicitud: " + serviceOffer.getTitle())
                        .appointmentId(updated.getAppointmentId())
                        .serviceOfferId(updated.getServiceOfferId())
                        .build());
            } else if (status == AppointmentStatus.COMPLETED) {
                notif = notificationService.createNotification(Notification.builder()
                        .userId(updated.getUserId())
                        .type(NotificationType.APPOINTMENT_COMPLETED)
                        .title("Servicio finalizado")
                        .message("Tu servicio " + serviceOffer.getTitle() + " fue finalizado. Deja tu reseña.")
                        .appointmentId(updated.getAppointmentId())
                        .serviceOfferId(updated.getServiceOfferId())
                        .build());
            }

            if (notif != null) {
                log.debug("[NOTIF] Notificación {} creada: id={} para userId={}",
                        status, notif.getId(), updated.getUserId());
            } else if (status == AppointmentStatus.ACCEPTED || status == AppointmentStatus.REJECTED
                    || status == AppointmentStatus.COMPLETED) {
                log.debug("[NOTIF] Notificación {} omitida (duplicada) para appointmentId={}", status, id);
            }
        } catch (Exception e) {
            log.error("[NOTIF] Error al crear notificación de status={} para appointmentId={}: {}",
                    status, id, e.getMessage(), e);
        }

        return updated;
    }
}