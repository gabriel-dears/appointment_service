package com.hospital_app.appointment_service.infra.swagger;

import com.hospital_app.appointment_service.infra.adapter.in.controller.dto.AppointmentResponseDto;
import com.hospital_app.appointment_service.infra.adapter.in.controller.dto.CreateAppointmentRequestDto;
import com.hospital_app.appointment_service.infra.adapter.in.controller.dto.UpdateAppointmentRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Tag(name = "Appointment", description = "Endpoints for managing hospital appointments")
public interface AppointmentApi {

    @Operation(
            summary = "Create a new appointment",
            description = "Creates a new appointment for a patient",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Appointment created",
                            content = @Content(schema = @Schema(implementation = AppointmentResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    default ResponseEntity<AppointmentResponseDto> createAppointment(
            @RequestBody @Valid CreateAppointmentRequestDto requestDto,
            UriComponentsBuilder uriComponentsBuilder) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Operation(
            summary = "Update an existing appointment",
            description = "Updates an appointment details by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Appointment updated",
                            content = @Content(schema = @Schema(implementation = AppointmentResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Appointment not found")
            }
    )
    default ResponseEntity<AppointmentResponseDto> updateAppointment(
            UUID id,
            @RequestBody @Valid UpdateAppointmentRequestDto requestDto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
