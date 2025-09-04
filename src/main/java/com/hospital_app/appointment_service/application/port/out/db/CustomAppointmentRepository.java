package com.hospital_app.appointment_service.application.port.out.db;

import com.hospital_app.appointment_service.domain.model.Appointment;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface CustomAppointmentRepository {

    Appointment create(Appointment appointment);

    Appointment update(Appointment appointment);

    Optional<Appointment> findById(UUID id);

    boolean isAppointmentTimeAvailableByDoctorOrPatientCreation(UUID patientId, UUID doctorId, LocalDateTime dateTime);

    boolean isAppointmentTimeAvailableByDoctorOrPatientUpdate(UUID patientId, UUID doctorId, UUID appointmentId, LocalDateTime dateTime);
}
