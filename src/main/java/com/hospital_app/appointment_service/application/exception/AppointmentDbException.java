package com.hospital_app.appointment_service.application.exception;

public class AppointmentDbException extends RuntimeException {
    public AppointmentDbException(String message, Throwable cause) {
        super(message, cause);
    }
}
