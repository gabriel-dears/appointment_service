package com.hospital_app.appointment_service.infra.adapter.out.message;

import com.hospital_app.appointment_service.application.command.UserInfo;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentMessageComposerPort;
import com.hospital_app.appointment_service.application.port.out.user_service.UserServiceClientPort;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.common.message.dto.AppointmentMessage;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMessageComposerAdapter implements AppointmentMessageComposerPort {

    private final UserServiceClientPort userServiceClientPort;

    public AppointmentMessageComposerAdapter(UserServiceClientPort userServiceClientPort) {
        this.userServiceClientPort = userServiceClientPort;
    }

    @Override
    public AppointmentMessage compose(Appointment appointment) {
        AppointmentMessage appointmentMessage = new AppointmentMessage();
        appointmentMessage.setId(appointment.getId());
        appointmentMessage.setPatientId(appointment.getPatientId());
        appointmentMessage.setDoctorId(appointment.getDoctorId());
        appointmentMessage.setDateTime(appointment.getDateTime());
        appointmentMessage.setNotes(appointment.getNotes());
        appointmentMessage.setVersion(appointment.getVersion());
        appointmentMessage.setStatus(appointment.getStatus().name());
        UserInfo doctorInfo = userServiceClientPort.getUserInfo(appointment.getDoctorId());
        UserInfo patientInfo = userServiceClientPort.getUserInfo(appointment.getPatientId());
        appointmentMessage.setDoctorName(doctorInfo.name());
        appointmentMessage.setPatientName(patientInfo.name());
        appointmentMessage.setPatientEmail(patientInfo.email());
        return appointmentMessage;
    }
}
