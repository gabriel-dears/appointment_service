package com.hospital_app.appointment_service.infra.adapter.out.db.jpa.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface JpaAppointmentRepository extends JpaRepository<JpaAppointmentEntity, UUID> {

    @Query("""
                SELECT COUNT(a) > 0
                FROM JpaAppointmentEntity a
                WHERE (a.doctorId = :doctorId OR a.patientId = :patientId)
                AND a.dateTime BETWEEN :startRange AND :endRange
                AND a.status IN ('CONFIRMED', 'CREATED', 'COMPLETED')
            """)
    boolean existsDateTimeConflict(@Param("doctorId") UUID doctorId,
                                   @Param("patientId") UUID patientId,
                                   @Param("startRange") LocalDateTime startRange,
                                   @Param("endRange") LocalDateTime endRange);

    @Query("""
                SELECT COUNT(a) > 0
                FROM JpaAppointmentEntity a
                WHERE a.id <> :appointmentId
                  AND (a.doctorId = :doctorId OR a.patientId = :patientId)
                  AND a.dateTime BETWEEN :startRange AND :endRange
                  AND a.status IN ('CONFIRMED', 'CREATED', 'COMPLETED')
            """)
    boolean existsDateTimeConflictDisregardingAppointment(
            @Param("appointmentId") UUID appointmentId,
            @Param("doctorId") UUID doctorId,
            @Param("patientId") UUID patientId,
            @Param("startRange") LocalDateTime startRange,
            @Param("endRange") LocalDateTime endRange
    );
}
