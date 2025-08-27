package com.hospital_app.appointment_service.application.service.appointment;

import com.hospital_app.appointment_service.application.mapper.AppointmentMapper;
import com.hospital_app.appointment_service.application.port.in.appointment.UpdateAppointmentUseCase;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.domain.exception.AppointmentNotFoundException;
import com.hospital_app.appointment_service.domain.model.Appointment;

import java.util.UUID;

public class UpdateAppointmentUseCaseImpl implements UpdateAppointmentUseCase {

    private final CustomAppointmentRepository customAppointmentRepository;

    public UpdateAppointmentUseCaseImpl(CustomAppointmentRepository customAppointmentRepository) {
        this.customAppointmentRepository = customAppointmentRepository;
    }

    @Override
    public Appointment execute(Appointment appointment, UUID id) {
        Appointment existingAppointment = customAppointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(String.format("Appointment with id %s not found", id)));
        return customAppointmentRepository.update(AppointmentMapper.fromInputToExistingAppointmentForUpdate(appointment, existingAppointment));
    }
}
