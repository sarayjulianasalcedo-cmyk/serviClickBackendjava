package com.sinquinto.serviclick.Appointment.Infrastructure.Repository;

import com.sinquinto.serviclick.Appointment.Domain.Appointment;
import com.sinquinto.serviclick.Appointment.Domain.AppointmentRepository;
import com.sinquinto.serviclick.Appointment.Infrastructure.Mapper.AppointmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final AppointmentJpaRepository jpaRepository;
    private final AppointmentMapper mapper;

    @Override
    public Appointment save(Appointment appointment) {
        return mapper.appointmentEntityToAppointment(
                jpaRepository.save(mapper.appointmentToEntity(appointment))
        );
    }

    @Override
    public Appointment findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::appointmentEntityToAppointment)
                .orElse(null);
    }

    @Override
    public List<Appointment> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::appointmentEntityToAppointment)
                .toList();
    }

    @Override
    public List<Appointment> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId)
                .stream()
                .map(mapper::appointmentEntityToAppointment)
                .toList();
    }

    @Override
    public List<Appointment> findByServiceOfferId(Long serviceOfferId) {
        return jpaRepository.findByServiceOfferId(serviceOfferId)
                .stream()
                .map(mapper::appointmentEntityToAppointment)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Long countAppointments() {
        return jpaRepository.count();
    }
}