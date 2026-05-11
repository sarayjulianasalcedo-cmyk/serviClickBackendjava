package com.sinquinto.serviclick.ServiceOffer.Infrastructure.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "service_offer")
public class ServiceOfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long serviceOfferId;

    Long sellerId;
    String title;
    String description;
    BigDecimal price;
    Integer estimatedDuration;
    String category;
    String photo;
    LocalDateTime publicationDate;
}