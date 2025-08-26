package com.hospital_app.appointment_service.infra.adapter.in.controller;

import com.hospital_app.appointment_service.application.port.in.appointment.CreateAppointmentUseCase;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.infra.adapter.in.controller.dto.AppointmentResponseDto;
import com.hospital_app.appointment_service.infra.adapter.in.controller.dto.CreateAppointmentRequestDto;
import com.hospital_app.appointment_service.infra.adapter.in.controller.mapper.DtoAppointmentMapper;
import com.hospital_app.appointment_service.infra.swagger.AppointmentApi;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("appointment")
public class AppointmentRestController implements AppointmentApi {

    private final CreateAppointmentUseCase createAppointmentUseCase;

    public AppointmentRestController(CreateAppointmentUseCase createAppointmentUseCase) {
        this.createAppointmentUseCase = createAppointmentUseCase;
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

}
