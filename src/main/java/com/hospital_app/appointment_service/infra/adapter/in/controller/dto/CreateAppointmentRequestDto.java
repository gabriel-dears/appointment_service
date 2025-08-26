package com.hospital_app.appointment_service.infra.adapter.in.controller.dto;

import com.hospital_app.appointment_service.domain.model.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAppointmentRequestDto(
        @NotNull(message = "Doctor id is required")
        UUID doctorId,

        @NotNull(message = "Patient id is required")
        UUID patientId,

        @NotNull(message = "Status is required")
        AppointmentStatus status,

        @NotBlank(message = "Notes are required")
        String notes,

        @NotNull(message = "Appointment date is required")
        @Future(message = "Appointment date must be in the future")
        LocalDateTime dateTime
) {}
