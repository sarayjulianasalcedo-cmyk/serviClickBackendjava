package com.sinquinto.serviclick.Appointment.Infrastructure;

import com.sinquinto.serviclick.Appointment.Application.AppointmentService;
import com.sinquinto.serviclick.Appointment.Domain.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping
    public ResponseEntity<Appointment> save(@RequestBody Appointment appointment) {
        return new ResponseEntity<>(service.saveAppointment(appointment), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAll() {
        return new ResponseEntity<>(service.findAllAppointments(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> findById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findAppointmentById(id), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Appointment>> findByUser(@PathVariable Long userId) {
        return new ResponseEntity<>(service.findAppointmentsByUser(userId), HttpStatus.OK);
    }

    @GetMapping("/service-offer/{serviceOfferId}")
    public ResponseEntity<List<Appointment>> findByServiceOffer(@PathVariable Long serviceOfferId) {
        return new ResponseEntity<>(service.findAppointmentsByServiceOffer(serviceOfferId), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<>(service.countAppointments(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> update(@PathVariable Long id, @RequestBody Appointment appointment) {
        return new ResponseEntity<>(service.updateAppointment(id, appointment), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteAppointmentById(id);
    }
}