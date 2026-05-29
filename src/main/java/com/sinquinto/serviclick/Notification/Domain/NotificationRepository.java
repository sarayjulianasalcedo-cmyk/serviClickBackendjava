package com.sinquinto.serviclick.Notification.Domain;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);
    List<Notification> findByUserId(Long userId);
    List<Notification> findByUserIdAndReadFalse(Long userId);
    Optional<Notification> findById(Long id);
    void markAsRead(Long id);
    void markAllAsReadByUserId(Long userId);
    boolean existsByAppointmentIdAndType(Long appointmentId, NotificationType type);
}