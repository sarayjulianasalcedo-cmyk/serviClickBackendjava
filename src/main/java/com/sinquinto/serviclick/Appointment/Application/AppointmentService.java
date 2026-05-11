package com.sinquinto.serviclick.Appointment.Application;

import com.sinquinto.serviclick.Appointment.Application.UseCases.AppointmentCountUseCase;
import com.sinquinto.serviclick.Appointment.Application.UseCases.AppointmentCrudUseCase;
import com.sinquinto.serviclick.Appointment.Domain.Appointment;
import com.sinquinto.serviclick.Appointment.Domain.AppointmentRepository;
import com.sinquinto.serviclick.Appointment.Domain.AppointmentStatus;
import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AppointmentService implements AppointmentCrudUseCase, AppointmentCountUseCase {

    private final AppointmentRepository repository;

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        appointment.setStatus(AppointmentStatus.PENDING);
        return repository.save(appointment);
    }

    @Override
    public Appointment findAppointmentById(Long id) {
        Appointment appointment = repository.findById(id);
        if (appointment == null) {
            throw new ResourceNotFoundException("Appointment with id " + id + " not found");
        }
        return appointment;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Appointment> findAllAppointments() {
        return repository.findAll();
    }

    @Override
    public List<Appointment> findAppointmentsByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<Appointment> findAppointmentsByServiceOffer(Long serviceOfferId) {
        return repository.findByServiceOfferId(serviceOfferId);
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment appointment) {
        Appointment appointmentDB = repository.findById(id);
        if (appointmentDB == null) {
            throw new ResourceNotFoundException("Appointment with id " + id + " not found");
        }
        appointmentDB.setDate(appointment.getDate());
        appointmentDB.setStatus(appointment.getStatus());
        return repository.save(appointmentDB);
    }

    @Override
    public void deleteAppointmentById(Long id) {
        Appointment appointmentDB = repository.findById(id);
        if (appointmentDB == null) {
            throw new ResourceNotFoundException("Appointment with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override
    public Long countAppointments() {
        return repository.countAppointments();
    }
}