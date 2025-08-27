package com.hospital_app.appointment_service.infra.adapter.out.db.jpa.appointment;

import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.infra.adapter.out.db.jpa.appointment.mapper.JpaAppointmentMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaCustomAppointmentRepository implements CustomAppointmentRepository {

    private final JpaAppointmentRepository jpaAppointmentRepository;
    private final JpaAppointmentMapper jpaAppointmentMapper;

    public JpaCustomAppointmentRepository(JpaAppointmentRepository jpaAppointmentRepository, JpaAppointmentMapper jpaAppointmentMapper) {
        this.jpaAppointmentRepository = jpaAppointmentRepository;
        this.jpaAppointmentMapper = jpaAppointmentMapper;
    }

    @Override
    public Appointment create(Appointment appointment) {
        return save(appointment);
    }

    @Override
    public Appointment update(Appointment appointment) {
        return save(appointment);
    }

    @Override
    public Optional<Appointment> findById(UUID id) {
        Optional<JpaAppointmentEntity> optionalAppointmentEntity = jpaAppointmentRepository.findById(id);
        return optionalAppointmentEntity.map(jpaAppointmentMapper::toDomain);
    }

    private Appointment save(Appointment appointment) {
        JpaAppointmentEntity entity = jpaAppointmentMapper.toEntity(appointment);
        JpaAppointmentEntity createdAppointment = jpaAppointmentRepository.save(entity);
        return jpaAppointmentMapper.toDomain(createdAppointment);
    }

}
