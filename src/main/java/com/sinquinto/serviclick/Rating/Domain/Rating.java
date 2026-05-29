package com.sinquinto.serviclick.Rating.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    Long ratingId;
    Long userId;
    Long serviceOfferId;
    Long appointmentId;
    Integer score;
    String comment;
    LocalDateTime date;
}