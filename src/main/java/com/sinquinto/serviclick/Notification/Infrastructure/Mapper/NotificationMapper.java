package com.sinquinto.serviclick.Notification.Infrastructure.Mapper;

import com.sinquinto.serviclick.Notification.Domain.Notification;
import com.sinquinto.serviclick.Notification.Infrastructure.Entity.NotificationEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationEntity toEntity(Notification notification) {
        return NotificationEntity.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.getRead() != null ? notification.getRead() : false)
                .appointmentId(notification.getAppointmentId())
                .serviceOfferId(notification.getServiceOfferId())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public Notification toDomain(NotificationEntity entity) {
        return Notification.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .type(entity.getType())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .read(entity.getRead())
                .appointmentId(entity.getAppointmentId())
                .serviceOfferId(entity.getServiceOfferId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}