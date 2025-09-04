package com.hospital_app.appointment_service.application.service.appointment;

import com.hospital_app.appointment_service.application.port.in.appointment.CreateAppointmentUseCase;
import com.hospital_app.appointment_service.application.port.in.validator.CreateAppointmentInputValidatorPort;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentMessageComposerPort;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentQueuePort;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.common.message.dto.AppointmentMessage;

public class CreateAppointmentUseCaseImpl implements CreateAppointmentUseCase {

    private final CustomAppointmentRepository customAppointmentRepository;
    private final AppointmentQueuePort appointmentQueuePort;
    private final AppointmentMessageComposerPort appointmentMessageComposerPort;
    private final CreateAppointmentInputValidatorPort createAppointmentInputValidatorPort;

    public CreateAppointmentUseCaseImpl(CustomAppointmentRepository customAppointmentRepository, AppointmentQueuePort appointmentQueuePort, AppointmentMessageComposerPort appointmentMessageComposerPort, CreateAppointmentInputValidatorPort createAppointmentInputValidatorPort) {
        this.customAppointmentRepository = customAppointmentRepository;
        this.appointmentQueuePort = appointmentQueuePort;
        this.appointmentMessageComposerPort = appointmentMessageComposerPort;
        this.createAppointmentInputValidatorPort = createAppointmentInputValidatorPort;
    }

    @Override
    public Appointment execute(Appointment appointment) {
        createAppointmentInputValidatorPort.validate(appointment);
        var createdAppointment = customAppointmentRepository.create(appointment);
        AppointmentMessage appointmentMessage = appointmentMessageComposerPort.compose(createdAppointment);
        appointmentQueuePort.sendAppointment(appointmentMessage);
        return createdAppointment;
    }


}
