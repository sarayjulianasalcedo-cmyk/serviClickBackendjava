package com.sinquinto.serviclick.ServiceOffer.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOffer {
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