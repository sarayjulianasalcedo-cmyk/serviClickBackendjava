package com.sinquinto.serviclick.Appointment.Infrastructure.DTO;

import com.sinquinto.serviclick.Appointment.Domain.AppointmentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentSellerDTO(
        Long appointmentId,
        Long serviceOfferId,
        String serviceTitle,
        String customerName,
        String customerEmail,
        LocalDateTime date,
        BigDecimal price,
        AppointmentStatus status
) {}