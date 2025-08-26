package com.hospital_app.appointment_service.application.port.out.db;

import com.hospital_app.appointment_service.domain.model.Appointment;

public interface CustomAppointmentRepository {

    Appointment create(Appointment appointment);

    Appointment update(Appointment appointment);

}
