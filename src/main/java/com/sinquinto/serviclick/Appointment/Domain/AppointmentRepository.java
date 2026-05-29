package com.sinquinto.serviclick.Appointment.Domain;

import java.util.List;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);
    Appointment findById(Long id);
    List<Appointment> findAll();
    List<Appointment> findByUserId(Long userId);
    List<Appointment> findByUserIdAndStatus(Long userId, AppointmentStatus status);
    List<Appointment> findByServiceOfferId(Long serviceOfferId);
    List<Appointment> findByServiceOfferIdIn(List<Long> serviceOfferIds);
    void deleteById(Long id);
    Long countAppointments();
}