package com.sinquinto.serviclick.Appointment.Infrastructure.DTO;

import com.sinquinto.serviclick.Appointment.Domain.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentStatusRequest {
    private AppointmentStatus status;
}