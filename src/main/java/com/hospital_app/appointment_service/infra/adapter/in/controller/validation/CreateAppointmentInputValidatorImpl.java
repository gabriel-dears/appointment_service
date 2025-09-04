package com.hospital_app.appointment_service.infra.adapter.in.controller.validation;

import com.hospital_app.appointment_service.application.exception.InvalidAppointmentCreate;
import com.hospital_app.appointment_service.application.port.in.validator.CreateAppointmentInputValidatorPort;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.application.port.out.user_service.UserServiceClientPort;
import com.hospital_app.appointment_service.domain.exception.DoctorNotFoundException;
import com.hospital_app.appointment_service.domain.exception.PatientNotFoundException;
import com.hospital_app.appointment_service.domain.model.Appointment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CreateAppointmentInputValidatorImpl implements CreateAppointmentInputValidatorPort {

    private final CustomAppointmentRepository customAppointmentRepository;
    private final UserServiceClientPort userServiceClientPort;

    public CreateAppointmentInputValidatorImpl(CustomAppointmentRepository customAppointmentRepository, UserServiceClientPort userServiceClientPort) {
        this.customAppointmentRepository = customAppointmentRepository;
        this.userServiceClientPort = userServiceClientPort;
    }

    public void validate(Appointment appointment) {
        if (!userServiceClientPort.isDoctorValid(appointment.getDoctorId())) {
            throw new DoctorNotFoundException(String.format("Doctor with id %s does not exist", appointment.getDoctorId()));
        }
        if (!userServiceClientPort.isPatientValid(appointment.getPatientId())) {
            throw new PatientNotFoundException(String.format("Patient with id %s does not exist", appointment.getPatientId()));
        }
        validateDateTimeRange(appointment);
    }

    private void validateDateTimeRange(Appointment appointment) {
        LocalDateTime dateTimeInput = appointment.getDateTime();

        if (
                dateTimeInput != null &&
                !customAppointmentRepository.isAppointmentTimeAvailableByDoctorOrPatientCreation(
                        appointment.getPatientId(),
                        appointment.getDoctorId(),
                        dateTimeInput
                )
        ) {
            throw new InvalidAppointmentCreate(
                    "The requested appointment time is unavailable. Either the doctor or the patient already has an appointment within 30 minutes of this time."
            );

        }
    }

}
