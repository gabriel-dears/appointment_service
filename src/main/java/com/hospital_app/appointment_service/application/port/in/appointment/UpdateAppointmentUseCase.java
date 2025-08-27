package com.hospital_app.appointment_service.application.port.in.appointment;

import com.hospital_app.appointment_service.domain.model.Appointment;

import java.util.UUID;

public interface UpdateAppointmentUseCase {
    Appointment execute(Appointment appointment, UUID id);
}
