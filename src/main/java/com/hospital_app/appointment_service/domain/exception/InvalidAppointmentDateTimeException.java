package com.hospital_app.appointment_service.domain.exception;

public class InvalidAppointmentDateTimeException extends RuntimeException {
    public InvalidAppointmentDateTimeException(String message) {
        super(message);
    }
}
