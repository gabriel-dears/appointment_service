package com.hospital_app.appointment_service.application.service.appointment;

import com.hospital_app.appointment_service.application.mapper.AppointmentMapper;
import com.hospital_app.appointment_service.application.port.in.appointment.UpdateAppointmentUseCase;
import com.hospital_app.appointment_service.application.port.in.validator.UpdateAppointmentInputValidatorPort;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentMessageComposerPort;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentQueuePort;
import com.hospital_app.appointment_service.domain.exception.AppointmentNotFoundException;
import com.hospital_app.appointment_service.domain.model.Appointment;

import java.util.Objects;
import java.util.UUID;

public class UpdateAppointmentUseCaseImpl implements UpdateAppointmentUseCase {

    private final CustomAppointmentRepository customAppointmentRepository;
    private final AppointmentQueuePort appointmentQueuePort;
    private final AppointmentMessageComposerPort appointmentMessageComposerPort;
    private final UpdateAppointmentInputValidatorPort updateAppointmentInputValidatorPort;

    public UpdateAppointmentUseCaseImpl(CustomAppointmentRepository customAppointmentRepository, AppointmentQueuePort appointmentQueuePort, AppointmentMessageComposerPort appointmentMessageComposerPort, UpdateAppointmentInputValidatorPort updateAppointmentInputValidatorPort) {
        this.customAppointmentRepository = customAppointmentRepository;
        this.appointmentQueuePort = appointmentQueuePort;
        this.appointmentMessageComposerPort = appointmentMessageComposerPort;
        this.updateAppointmentInputValidatorPort = updateAppointmentInputValidatorPort;
    }

    @Override
    public Appointment execute(Appointment appointment, UUID id) {
        Appointment existingAppointment = customAppointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(String.format("Appointment with id %s not found", id)));
        if (shouldExecuteUpdate(appointment, existingAppointment)) {
            updateAppointmentInputValidatorPort.validate(existingAppointment, appointment);
            var updatedAppointment = customAppointmentRepository.update(AppointmentMapper.fromInputToExistingAppointmentForUpdate(appointment, existingAppointment));
            appointmentQueuePort.sendAppointment(appointmentMessageComposerPort.compose(updatedAppointment));
            return updatedAppointment;
        }
        return existingAppointment;
    }

    private boolean shouldExecuteUpdate(Appointment appointment, Appointment existingAppointment) {

        if (appointment.getStatus() != existingAppointment.getStatus()) {
            return true;
        }

        if (!Objects.equals(appointment.getNotes(), existingAppointment.getNotes())) {
            return true;
        }

        return appointment.getDateTime() != existingAppointment.getDateTime() && appointment.getDateTime() != null;
    }

}
