package com.sinquinto.serviclick.ServiceOffer.Infrastructure.Repository;

import com.sinquinto.serviclick.ServiceOffer.Infrastructure.Entity.ServiceOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceOfferJpaRepository extends JpaRepository<ServiceOfferEntity, Long> {
    List<ServiceOfferEntity> findBySellerId(Long sellerId);
}