package com.hospital_app.appointment_service.infra.adapter.in.controller.dto;

import com.hospital_app.appointment_service.domain.model.AppointmentStatus;

import java.util.UUID;

public record AppointmentResponseDto(
        UUID id,
        UUID doctorId,
        UUID patientId,
        AppointmentStatus status,
        String notes,
        String dateTime
) {
}
