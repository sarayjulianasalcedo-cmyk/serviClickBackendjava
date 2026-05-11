package com.sinquinto.serviclick.Appointment.Infrastructure.Entity;

import com.sinquinto.serviclick.Appointment.Domain.AppointmentStatus;
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
@Table(name = "appointment")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long appointmentId;

    Long userId;
    Long serviceOfferId;
    LocalDateTime date;

    @Enumerated(EnumType.STRING)
    AppointmentStatus status;
}