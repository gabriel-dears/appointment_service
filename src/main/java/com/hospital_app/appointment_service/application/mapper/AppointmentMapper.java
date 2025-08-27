package com.hospital_app.appointment_service.application.mapper;

import com.hospital_app.appointment_service.domain.model.Appointment;

public class AppointmentMapper {

    public static Appointment fromInputToExistingAppointmentForUpdate(Appointment appointment, Appointment existingAppointment) {
        existingAppointment.setNotes(appointment.getNotes());
        existingAppointment.setStatus(appointment.getStatus());
        existingAppointment.setDateTime(appointment.getDateTime());
        return existingAppointment;
    }

}
