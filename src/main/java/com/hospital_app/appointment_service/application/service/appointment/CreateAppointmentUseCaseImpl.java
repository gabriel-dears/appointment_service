package com.hospital_app.appointment_service.application.service.appointment;

import com.hospital_app.appointment_service.application.port.in.appointment.CreateAppointmentUseCase;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentMessageComposerPort;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentQueuePort;
import com.hospital_app.appointment_service.application.port.out.user_service.UserServiceClientPort;
import com.hospital_app.appointment_service.domain.exception.DoctorNotFoundException;
import com.hospital_app.appointment_service.domain.exception.PatientNotFoundException;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.common.message.dto.AppointmentMessage;

public class CreateAppointmentUseCaseImpl implements CreateAppointmentUseCase {

    private final CustomAppointmentRepository customAppointmentRepository;
    private final UserServiceClientPort userServiceClientPort;
    private final AppointmentQueuePort appointmentQueuePort;
    private final AppointmentMessageComposerPort appointmentMessageComposerPort;

    public CreateAppointmentUseCaseImpl(CustomAppointmentRepository customAppointmentRepository, UserServiceClientPort userServiceClientPort, AppointmentQueuePort appointmentQueuePort, AppointmentMessageComposerPort appointmentMessageComposerPort) {
        this.customAppointmentRepository = customAppointmentRepository;
        this.userServiceClientPort = userServiceClientPort;
        this.appointmentQueuePort = appointmentQueuePort;
        this.appointmentMessageComposerPort = appointmentMessageComposerPort;
    }

    @Override
    public Appointment execute(Appointment appointment) {
        validate(appointment);
        var createdAppointment = customAppointmentRepository.create(appointment);
        AppointmentMessage appointmentMessage = appointmentMessageComposerPort.compose(createdAppointment);
        appointmentQueuePort.sendAppointment(appointmentMessage);
        return createdAppointment;
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
