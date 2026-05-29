package com.sinquinto.serviclick.Notification.Application;

import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import com.sinquinto.serviclick.Notification.Domain.Notification;
import com.sinquinto.serviclick.Notification.Domain.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository repository;

    public Notification createNotification(Notification notification) {
        log.debug("[NOTIF-SVC] Intentando crear notificación: type={}, userId={}, appointmentId={}, serviceOfferId={}",
                notification.getType(), notification.getUserId(),
                notification.getAppointmentId(), notification.getServiceOfferId());

        if (notification.getAppointmentId() != null &&
            repository.existsByAppointmentIdAndType(notification.getAppointmentId(), notification.getType())) {
            log.debug("[NOTIF-SVC] Duplicada, se omite: type={}, appointmentId={}",
                    notification.getType(), notification.getAppointmentId());
            return null;
        }

        notification.setRead(false);
        Notification saved = repository.save(notification);
        log.debug("[NOTIF-SVC] Notificación guardada con id={}", saved.getId());
        return saved;
    }

    public List<Notification> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public List<Notification> findUnreadByUserId(Long userId) {
        return repository.findByUserIdAndReadFalse(userId);
    }

    @Transactional
    public Notification markAsRead(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification with id " + id + " not found"));
        repository.markAsRead(id);
        notification.setRead(true);
        return notification;
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        repository.markAllAsReadByUserId(userId);
    }
}