package com.sinquinto.serviclick.Appointment.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    Long appointmentId;
    Long userId;
    Long serviceOfferId;
    LocalDateTime date;
    AppointmentStatus status;
}