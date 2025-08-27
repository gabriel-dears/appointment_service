package com.hospital_app.appointment_service.infra.adapter.in.controller;

import com.hospital_app.appointment_service.application.port.in.appointment.CreateAppointmentUseCase;
import com.hospital_app.appointment_service.application.port.in.appointment.UpdateAppointmentUseCase;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.infra.adapter.in.controller.dto.AppointmentResponseDto;
import com.hospital_app.appointment_service.infra.adapter.in.controller.dto.CreateAppointmentRequestDto;
import com.hospital_app.appointment_service.infra.adapter.in.controller.dto.UpdateAppointmentRequestDto;
import com.hospital_app.appointment_service.infra.adapter.in.controller.mapper.DtoAppointmentMapper;
import com.hospital_app.appointment_service.infra.swagger.AppointmentApi;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("appointment")
public class AppointmentRestController implements AppointmentApi {

    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final UpdateAppointmentUseCase updateAppointmentUseCase;

    public AppointmentRestController(CreateAppointmentUseCase createAppointmentUseCase, UpdateAppointmentUseCase updateAppointmentUseCase) {
        this.createAppointmentUseCase = createAppointmentUseCase;
        this.updateAppointmentUseCase = updateAppointmentUseCase;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> createAppointment(@RequestBody @Valid CreateAppointmentRequestDto requestDto, UriComponentsBuilder uriComponentsBuilder) {
        Appointment createdAppointment = createAppointmentUseCase.execute(DtoAppointmentMapper.toDomain(requestDto));
        URI location = uriComponentsBuilder
                .path("/appointment/{id}")
                .buildAndExpand(createdAppointment.getId())
                .toUri();
        return ResponseEntity.created(uriComponentsBuilder.build(location)).body(DtoAppointmentMapper.toResponseDto(createdAppointment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(@PathVariable UUID id, @RequestBody @Valid UpdateAppointmentRequestDto updateRequestDto) {
        Appointment appointment = updateAppointmentUseCase.execute(DtoAppointmentMapper.toDomain(updateRequestDto), id);
        return ResponseEntity.ok(DtoAppointmentMapper.toResponseDto(appointment));
    }

}
