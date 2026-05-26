package com.sinquinto.serviclick.Appointment.Infrastructure.Repository;

import com.sinquinto.serviclick.Appointment.Domain.AppointmentStatus;
import com.sinquinto.serviclick.Appointment.Infrastructure.Entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findByUserId(Long userId);
    List<AppointmentEntity> findByUserIdAndStatus(Long userId, AppointmentStatus status);
    List<AppointmentEntity> findByServiceOfferId(Long serviceOfferId);
    List<AppointmentEntity> findByServiceOfferIdIn(List<Long> serviceOfferIds);
}