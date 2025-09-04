package com.hospital_app.appointment_service.application.exception;

public class InvalidAppointmentUpdate extends RuntimeException {
    public InvalidAppointmentUpdate(String message) {
        super(message);
    }
}
