package com.hospital_app.appointment_service.application.exception;

public class InvalidAppointmentCreate extends RuntimeException {
    public InvalidAppointmentCreate(String message) {
        super(message);
    }
}
