package com.hospital_app.appointment_service.infra.adapter.in.controller;

import com.hospital_app.appointment_service.domain.model.Appointment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("appointment")
public class AppointmentRestController {

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(Appointment appointment) {
        return ResponseEntity.ok(appointment);
    }

}
