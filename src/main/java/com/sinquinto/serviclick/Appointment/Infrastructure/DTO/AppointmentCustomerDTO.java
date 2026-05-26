package com.sinquinto.serviclick.Appointment.Infrastructure.DTO;

import com.sinquinto.serviclick.Appointment.Domain.AppointmentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentCustomerDTO(
        Long appointmentId,
        Long serviceOfferId,
        String serviceTitle,
        String sellerName,
        String category,
        LocalDateTime date,
        BigDecimal price,
        AppointmentStatus status,
        boolean hasRating
) {}