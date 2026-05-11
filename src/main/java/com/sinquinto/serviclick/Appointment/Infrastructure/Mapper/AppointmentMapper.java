package com.sinquinto.serviclick.Appointment.Infrastructure.Mapper;

import com.sinquinto.serviclick.Appointment.Domain.Appointment;
import com.sinquinto.serviclick.Appointment.Infrastructure.Entity.AppointmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    Appointment appointmentEntityToAppointment(AppointmentEntity entity);
    AppointmentEntity appointmentToEntity(Appointment appointment);
}