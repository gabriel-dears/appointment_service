package com.hospital_app.appointment_service.application.mapper;

import com.hospital_app.appointment_service.domain.model.Appointment;

public class AppointmentMapper {

    public static Appointment fromInputToExistingAppointmentForUpdate(Appointment appointment, Appointment existingAppointment) {
        if (appointment.getDateTime() != null) {
            existingAppointment.setDateTime(appointment.getDateTime());
        }
        existingAppointment.setNotes(appointment.getNotes());
        existingAppointment.setStatus(appointment.getStatus());
        return existingAppointment;
    }

}
