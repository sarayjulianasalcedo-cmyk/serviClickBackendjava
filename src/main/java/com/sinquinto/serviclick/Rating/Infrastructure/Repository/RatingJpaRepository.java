package com.sinquinto.serviclick.Rating.Infrastructure.Repository;

import com.sinquinto.serviclick.Rating.Infrastructure.Entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingJpaRepository extends JpaRepository<RatingEntity, Long> {
    List<RatingEntity> findByServiceOfferId(Long serviceOfferId);
    List<RatingEntity> findByUserId(Long userId);
    boolean existsByAppointmentId(Long appointmentId);
}