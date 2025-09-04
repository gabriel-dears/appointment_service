package com.hospital_app.appointment_service.infra.adapter.out.db.jpa.appointment;

import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.domain.model.AppointmentStatus;
import com.hospital_app.appointment_service.infra.adapter.out.db.jpa.appointment.mapper.JpaAppointmentMapper;
import com.hospital_app.appointment_service.infra.db.AppointmentDbOperationWrapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
        appointment.setStatus(AppointmentStatus.CREATED);
        return save(appointment);
    }

    @Override
    public Appointment update(Appointment appointment) {
        return save(appointment);
    }

    @Override
    public Optional<Appointment> findById(UUID id) {
        Optional<JpaAppointmentEntity> optionalAppointmentEntity = AppointmentDbOperationWrapper.execute(() -> jpaAppointmentRepository.findById(id));
        return optionalAppointmentEntity.map(jpaAppointmentMapper::toDomain);
    }

    @Override
    public boolean isAppointmentTimeAvailableByDoctorOrPatientCreation(UUID patientId, UUID doctorId, LocalDateTime dateTime) {

        LocalDateTime dateTimePlus30Minutes = dateTime.plusMinutes(30);
        LocalDateTime dateTimeMinus30Minutes = dateTime.minusMinutes(30);

        return !jpaAppointmentRepository.existsDateTimeConflict(
                doctorId,
                patientId,
                dateTimeMinus30Minutes,
                dateTimePlus30Minutes
        );
    }

    @Override
    public boolean isAppointmentTimeAvailableByDoctorOrPatientUpdate(UUID patientId, UUID doctorId, UUID appointmentId, LocalDateTime dateTime) {

        LocalDateTime dateTimePlus30Minutes = dateTime.plusMinutes(30);
        LocalDateTime dateTimeMinus30Minutes = dateTime.minusMinutes(30);

        return !jpaAppointmentRepository.existsDateTimeConflictDisregardingAppointment(
                doctorId,
                patientId,
                appointmentId,
                dateTimeMinus30Minutes,
                dateTimePlus30Minutes
        );
    }

    private Appointment save(Appointment appointment) {
        JpaAppointmentEntity createdAppointment = AppointmentDbOperationWrapper.execute(() -> {
            JpaAppointmentEntity entity = jpaAppointmentMapper.toEntity(appointment);
            return jpaAppointmentRepository.save(entity);
        });
        return jpaAppointmentMapper.toDomain(createdAppointment);
    }

}
