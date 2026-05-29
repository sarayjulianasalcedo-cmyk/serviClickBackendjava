package com.sinquinto.serviclick.Notification.Infrastructure.Repository;

import com.sinquinto.serviclick.Notification.Domain.NotificationType;
import com.sinquinto.serviclick.Notification.Infrastructure.Entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<NotificationEntity> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);

    boolean existsByAppointmentIdAndType(Long appointmentId, NotificationType type);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.read = true WHERE n.userId = :userId AND n.read = false")
    void markAllAsReadByUserId(@Param("userId") Long userId);
}