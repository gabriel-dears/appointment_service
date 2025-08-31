package com.hospital_app.appointment_service.infra.adapter.out.db.jpa.appointment.mapper;

import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.infra.adapter.out.db.jpa.appointment.JpaAppointmentEntity;
import org.springframework.stereotype.Component;

@Component
public class JpaAppointmentMapper {

    public JpaAppointmentEntity toEntity(Appointment appointment) {
        return new JpaAppointmentEntity(
                appointment.getId(),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getDateTime(),
                appointment.getStatus(),
                appointment.getVersion(),
                appointment.getNotes()
        );
    }

    public Appointment toDomain(JpaAppointmentEntity jpaAppointmentEntity) {
        Appointment appointment = new Appointment();
        appointment.setId(jpaAppointmentEntity.getId());
        appointment.setDoctorId(jpaAppointmentEntity.getDoctorId());
        appointment.setPatientId(jpaAppointmentEntity.getPatientId());
        appointment.setDateTime(jpaAppointmentEntity.getDateTime());
        appointment.setVersion(jpaAppointmentEntity.getVersion());
        appointment.setStatus(jpaAppointmentEntity.getStatus());
        appointment.setNotes(jpaAppointmentEntity.getNotes());
        return appointment;
    }
}
