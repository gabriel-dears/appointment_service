package com.hospital_app.appointment_service.application.validation;

import com.hospital_app.appointment_service.application.exception.InvalidAppointmentUpdate;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.domain.model.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.Set;

public class UpdateAppointmentInputConfirmedStatusValidation implements UpdateAppointmentInputValidation {
    @Override
    public void validate(Appointment existingAppointment, Appointment appointment) {

        if (AppointmentStatus.CREATED == appointment.getStatus()) {
            throw new InvalidAppointmentUpdate("Current appointment status is CONFIRMED and cannot be changed to CREATED. Please, cancel the appointment and create another one.");
        }

        LocalDateTime now = LocalDateTime.now();

        if ((appointment.getDateTime() != null && appointment.getDateTime().isAfter(now)) || existingAppointment.getDateTime().isAfter(now)) {
            Set<AppointmentStatus> invalidStatusChangeInTheFuture = Set.of(AppointmentStatus.NO_SHOW, AppointmentStatus.COMPLETED);
            if (invalidStatusChangeInTheFuture.contains(appointment.getStatus())) {
                throw new InvalidAppointmentUpdate(
                        "Appointment scheduled for the future cannot be marked as COMPLETED or NO_SHOW."
                );
            }
        }

    }
}
