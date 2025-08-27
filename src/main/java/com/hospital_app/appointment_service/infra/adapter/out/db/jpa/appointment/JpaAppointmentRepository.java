package com.hospital_app.appointment_service.infra.adapter.out.db.jpa.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaAppointmentRepository extends JpaRepository<JpaAppointmentEntity, UUID> {
}
