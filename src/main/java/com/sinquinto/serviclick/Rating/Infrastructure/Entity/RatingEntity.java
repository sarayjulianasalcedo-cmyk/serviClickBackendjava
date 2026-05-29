package com.sinquinto.serviclick.Rating.Infrastructure.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rating")
public class RatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long ratingId;

    Long userId;
    Long serviceOfferId;
    Long appointmentId;
    Integer score;
    String comment;
    LocalDateTime date;
}