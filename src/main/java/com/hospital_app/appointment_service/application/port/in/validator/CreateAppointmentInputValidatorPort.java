package com.hospital_app.appointment_service.application.port.in.validator;

import com.hospital_app.appointment_service.domain.model.Appointment;

public interface CreateAppointmentInputValidatorPort {

    void validate(Appointment appointment);

}
