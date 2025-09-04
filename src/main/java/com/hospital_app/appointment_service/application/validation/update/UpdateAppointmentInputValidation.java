package com.hospital_app.appointment_service.application.validation.update;

import com.hospital_app.appointment_service.domain.model.Appointment;

public interface UpdateAppointmentInputValidation {
    void validate(Appointment existingAppointment, Appointment appointment);
}
