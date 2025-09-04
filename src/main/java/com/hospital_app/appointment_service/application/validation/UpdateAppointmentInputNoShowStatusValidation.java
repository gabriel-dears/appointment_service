package com.hospital_app.appointment_service.application.validation;

import com.hospital_app.appointment_service.application.exception.InvalidAppointmentUpdate;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.domain.model.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.Set;

public class UpdateAppointmentInputNoShowStatusValidation implements UpdateAppointmentInputValidation {
    @Override
    public void validate(Appointment existingAppointment, Appointment appointment) {

        if( existingAppointment.getStatus() != appointment.getStatus() ) {
            throw new InvalidAppointmentUpdate("Current appointment status cannot be changed.");
        }

    }
}
