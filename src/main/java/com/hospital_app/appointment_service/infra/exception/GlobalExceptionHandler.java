package com.hospital_app.appointment_service.infra.exception;

import com.hospital_app.appointment_service.domain.exception.DoctorNotFoundException;
import com.hospital_app.appointment_service.domain.exception.InvalidAppointmentDateTimeException;
import com.hospital_app.appointment_service.domain.exception.PatientNotFoundException;
import com.hospital_app.common.exception.GlobalExceptionHandlerBase;
import com.hospital_app.common.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends GlobalExceptionHandlerBase {

    @ExceptionHandler(value = {
            PatientNotFoundException.class,
            DoctorNotFoundException.class
    })
    @Override
    public ResponseEntity<ErrorResponse> handleInputNotFoundErrors(
            Exception exception,
            HttpServletRequest request) {
        return super.handleInputNotFoundErrors(exception, request);
    }

    @ExceptionHandler(value = {
            InvalidAppointmentDateTimeException.class
    })
    @Override
    public ResponseEntity<ErrorResponse> handleCustomInputErrors(
            Exception exception,
            HttpServletRequest request) {
        return super.handleCustomInputErrors(exception, request);
    }

}
