package com.hospital_app.appointment_service.infra.adapter.out.db.jpa.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAppointmentRepository extends JpaRepository<JpaAppointmentEntity, Long> {
}
