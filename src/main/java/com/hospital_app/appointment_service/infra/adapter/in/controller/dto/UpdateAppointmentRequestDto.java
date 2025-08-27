package com.hospital_app.appointment_service.infra.adapter.in.controller.dto;

import com.hospital_app.appointment_service.domain.model.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateAppointmentRequestDto(
        @NotNull(message = "Status is required")
        AppointmentStatus status,

        @NotBlank(message = "Notes are required")
        String notes,

        @NotNull(message = "Appointment date is required")
        @Future(message = "Appointment date must be in the future")
        LocalDateTime dateTime
) {}
