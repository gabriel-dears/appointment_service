package com.hospital_app.appointment_service.infra.swagger;

import com.hospital_app.appointment_service.infra.adapter.in.controller.dto.AppointmentResponseDto;
import com.hospital_app.appointment_service.infra.adapter.in.controller.dto.CreateAppointmentRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

public interface AppointmentApi {

    default ResponseEntity<AppointmentResponseDto> createAppointment(@RequestBody @Valid CreateAppointmentRequestDto requestDto, UriComponentsBuilder uriComponentsBuilder) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    default ResponseEntity<AppointmentResponseDto> updateAppointment(@RequestBody @Valid CreateAppointmentRequestDto requestDto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
