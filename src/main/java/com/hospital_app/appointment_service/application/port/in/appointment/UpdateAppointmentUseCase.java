package com.hospital_app.appointment_service.application.port.in.appointment;

import com.hospital_app.appointment_service.domain.model.Appointment;

public interface UpdateAppointmentUseCase {
    Appointment execute(Appointment appointment);
}
