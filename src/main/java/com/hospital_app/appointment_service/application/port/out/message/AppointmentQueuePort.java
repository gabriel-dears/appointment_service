package com.hospital_app.appointment_service.application.port.out.message;


import com.hospital_app.common.message.dto.AppointmentMessage;

public interface AppointmentQueuePort {
    void sendAppointment(AppointmentMessage appointmentMessage);
}
