package com.hospital_app.appointment_service.application.port.out.message;

import com.hospital_app.appointment_service.domain.model.Appointment;

public interface AppointmentQueuePort {
    void sendAppointment(Appointment appointment);
}
