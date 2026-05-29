package com.sinquinto.serviclick.Rating.Infrastructure.DTO;

import java.time.LocalDateTime;

public record RatingDTO(
        String clientName,
        Integer rating,
        String comment,
        LocalDateTime createdAt
) {}
