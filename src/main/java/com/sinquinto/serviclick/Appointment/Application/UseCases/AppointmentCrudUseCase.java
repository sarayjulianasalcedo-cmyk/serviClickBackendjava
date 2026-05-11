package com.sinquinto.serviclick.Appointment.Application.UseCases;

import com.sinquinto.serviclick.Appointment.Domain.Appointment;
import java.util.List;

public interface AppointmentCrudUseCase {
    Appointment saveAppointment(Appointment appointment);
    Appointment findAppointmentById(Long id);
    List<Appointment> findAllAppointments();
    List<Appointment> findAppointmentsByUser(Long userId);
    List<Appointment> findAppointmentsByServiceOffer(Long serviceOfferId);
    Appointment updateAppointment(Long id, Appointment appointment);
    void deleteAppointmentById(Long id);
}