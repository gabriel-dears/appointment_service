package com.hospital_app.appointment_service.application.service;

import com.hospital_app.appointment_service.application.port.in.appointment.CreateAppointmentUseCase;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.application.port.out.user_service.UserServiceClientPort;
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
        if(!userServiceClientPort.isDoctorValid(appointment.getDoctorId())) {
            // TODO: throw custom exception
        }

        if(userServiceClientPort.isPatientValid(appointment.getPatientId())) {
            // TODO: throw custom exception
        }

        // TODO: create common module for common classes -> not about security

        return customAppointmentRepository.create(appointment);
    }
}
