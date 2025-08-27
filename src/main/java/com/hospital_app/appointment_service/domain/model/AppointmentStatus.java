package com.hospital_app.appointment_service.domain.model;

public enum AppointmentStatus {
    CREATED,      // Appointment scheduled but not yet confirmed
    CONFIRMED,    // Appointment confirmed
    COMPLETED,    // Appointment finished successfully
    CANCELLED,    // Appointment cancelled before it started
    NO_SHOW       // Patient did not show up
}

