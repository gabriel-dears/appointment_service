package com.hospital_app.appointment_service.application.validation.update;

import com.hospital_app.appointment_service.application.exception.InvalidAppointmentUpdate;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.domain.model.AppointmentStatus;

import java.util.Set;

public class UpdateAppointmentInputCreatedStatusValidation implements UpdateAppointmentInputValidation {
    @Override
    public void validate(Appointment existingAppointment, Appointment appointment) {

        if (isInvalidStatus(appointment)) {
            throw new InvalidAppointmentUpdate("Appointment status cannot be changed from CREATED to NO_SHOW or COMPLETED.");
        }

    }

    private static boolean isInvalidStatus(Appointment appointment) {
        Set<AppointmentStatus> invalidStatusSet = Set.of(AppointmentStatus.NO_SHOW, AppointmentStatus.COMPLETED);
        return invalidStatusSet.contains(appointment.getStatus());
    }
}
