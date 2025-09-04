package com.hospital_app.appointment_service.infra.adapter.in.controller.validation;

import com.hospital_app.appointment_service.application.port.in.validator.UpdateAppointmentInputValidatorPort;
import com.hospital_app.appointment_service.application.validation.UpdateAppointmentInputValidationFactory;
import com.hospital_app.appointment_service.domain.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class UpdateAppointmentInputValidatorImpl implements UpdateAppointmentInputValidatorPort {

    public void validate(Appointment existingAppointment, Appointment appointment) {
        UpdateAppointmentInputValidationFactory.getValidation(existingAppointment.getStatus()).validate(existingAppointment, appointment);

        // TODO: validate -> any appointment in a 30 future and 30 minutes past range for the same doctorId

    }

}
