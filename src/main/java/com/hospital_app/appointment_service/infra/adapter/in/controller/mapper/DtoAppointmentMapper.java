package com.hospital_app.appointment_service.infra.adapter.in.controller.mapper;

import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.infra.adapter.in.controller.dto.AppointmentResponseDto;
import com.hospital_app.appointment_service.infra.adapter.in.controller.dto.CreateAppointmentRequestDto;
import com.hospital_app.appointment_service.infra.adapter.in.controller.dto.UpdateAppointmentRequestDto;

import java.time.format.DateTimeFormatter;

public class DtoAppointmentMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Appointment toDomain(CreateAppointmentRequestDto dto) {
        Appointment appointment = new Appointment();
        appointment.setDoctorId(dto.doctorId());
        appointment.setPatientId(dto.patientId());
        appointment.setStatus(dto.status());
        appointment.setNotes(dto.notes());
        appointment.setDateTime(dto.dateTime());
        return appointment;
    }

    public static AppointmentResponseDto toResponseDto(Appointment appointment) {
        return new AppointmentResponseDto(
                appointment.getId(),
                appointment.getDoctorId(),
                appointment.getPatientId(),
                appointment.getStatus(),
                appointment.getNotes(),
                appointment.getDateTime().format(formatter)
        );
    }

    public static Appointment toDomain(UpdateAppointmentRequestDto updateAppointmentUseCase) {
        Appointment appointment = new Appointment();
        appointment.setStatus(updateAppointmentUseCase.status());
        appointment.setNotes(updateAppointmentUseCase.notes());
        appointment.setDateTime(updateAppointmentUseCase.dateTime());
        return appointment;
    }
}
