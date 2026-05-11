package com.sinquinto.serviclick.Appointment.Infrastructure.Repository;

import com.sinquinto.serviclick.Appointment.Infrastructure.Entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findByUserId(Long userId);
    List<AppointmentEntity> findByServiceOfferId(Long serviceOfferId);
}