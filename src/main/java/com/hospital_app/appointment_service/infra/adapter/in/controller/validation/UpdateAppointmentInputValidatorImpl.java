package com.hospital_app.appointment_service.infra.adapter.in.controller.validation;

import com.hospital_app.appointment_service.application.exception.InvalidAppointmentUpdate;
import com.hospital_app.appointment_service.application.port.in.validator.UpdateAppointmentInputValidatorPort;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.application.validation.update.UpdateAppointmentInputValidationFactory;
import com.hospital_app.appointment_service.domain.model.Appointment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UpdateAppointmentInputValidatorImpl implements UpdateAppointmentInputValidatorPort {

    private final CustomAppointmentRepository customAppointmentRepository;

    public UpdateAppointmentInputValidatorImpl(CustomAppointmentRepository customAppointmentRepository) {
        this.customAppointmentRepository = customAppointmentRepository;
    }

    public void validate(Appointment existingAppointment, Appointment appointment) {
        UpdateAppointmentInputValidationFactory.getValidation(existingAppointment.getStatus()).validate(existingAppointment, appointment);
        validateDateTimeRange(existingAppointment, appointment);
    }

    private void validateDateTimeRange(Appointment existingAppointment, Appointment appointment) {
        LocalDateTime dateTimeInput = appointment.getDateTime();

        if (
                dateTimeInput != null &&
                !customAppointmentRepository.isAppointmentTimeAvailableByDoctorOrPatientUpdate(
                        existingAppointment.getPatientId(),
                        existingAppointment.getDoctorId(),
                        existingAppointment.getId(),
                        dateTimeInput
                )
        ) {
            throw new InvalidAppointmentUpdate(
                    "The requested appointment time is unavailable. Either the doctor or the patient already has an appointment within 30 minutes of this time."
            );

        }
    }

}
