package com.hospital_app.appointment_service.application.service;

import com.hospital_app.appointment_service.application.port.in.appointment.CreateAppointmentUseCase;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.application.port.out.user_service.UserServiceClientPort;
import com.hospital_app.appointment_service.domain.exception.DoctorNotFoundException;
import com.hospital_app.appointment_service.domain.exception.PatientNotFoundException;
import com.hospital_app.appointment_service.domain.model.Appointment;

public class CreateAppointmentUseCaseImpl implements CreateAppointmentUseCase {

    private final CustomAppointmentRepository customAppointmentRepository;
    private final UserServiceClientPort userServiceClientPort;

    public CreateAppointmentUseCaseImpl(CustomAppointmentRepository customAppointmentRepository, UserServiceClientPort userServiceClientPort) {
        this.customAppointmentRepository = customAppointmentRepository;
        this.userServiceClientPort = userServiceClientPort;
    }

    @Override
    public Appointment execute(Appointment appointment) {
        validate(appointment);
        return customAppointmentRepository.create(appointment);

        // TODO: Send message to kafka: notification and appointment-history service
        // TODO: After create and update flows
        // TODO: Think about how to consult information -> don't expose anything in the appointment-service, only via appointment-history
        // TODO: Stop exposing ports via docker for the internal service and expose kong port only... as well as config it accordingly
        // TODO: Swagger documentation
    }

    private void validate(Appointment appointment) {
        if (!userServiceClientPort.isDoctorValid(appointment.getDoctorId())) {
            throw new DoctorNotFoundException(String.format("Doctor with id %s does not exist", appointment.getDoctorId()));
        }
        if (!userServiceClientPort.isPatientValid(appointment.getPatientId())) {
            throw new PatientNotFoundException(String.format("Patient with id %s does not exist", appointment.getPatientId()));
        }
    }
}
