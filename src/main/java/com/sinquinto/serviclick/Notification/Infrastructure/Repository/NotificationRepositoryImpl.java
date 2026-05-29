package com.sinquinto.serviclick.Notification.Infrastructure.Repository;

import com.sinquinto.serviclick.Notification.Domain.Notification;
import com.sinquinto.serviclick.Notification.Domain.NotificationRepository;
import com.sinquinto.serviclick.Notification.Domain.NotificationType;
import com.sinquinto.serviclick.Notification.Infrastructure.Mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;
    private final NotificationMapper mapper;

    @Override
    public Notification save(Notification notification) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(notification)));
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Notification> findByUserIdAndReadFalse(Long userId) {
        return jpaRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setRead(true);
            jpaRepository.save(entity);
        });
    }

    @Override
    @Transactional
    public void markAllAsReadByUserId(Long userId) {
        jpaRepository.markAllAsReadByUserId(userId);
    }

    @Override
    public boolean existsByAppointmentIdAndType(Long appointmentId, NotificationType type) {
        return jpaRepository.existsByAppointmentIdAndType(appointmentId, type);
    }
}
