package com.hospital_app.appointment_service.application.service.appointment;

import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.domain.exception.AppointmentNotFoundException;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.domain.model.AppointmentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAppointmentUseCaseImplTest {

    @Mock
    private CustomAppointmentRepository customAppointmentRepository;

    @InjectMocks
    private UpdateAppointmentUseCaseImpl updateAppointmentUseCase;

    @Test
    void shouldNotUpdateWithInvalidAppointmentId() {
        UUID id = UUID.randomUUID();
        Appointment appointment = getAppointment();
        when(customAppointmentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(AppointmentNotFoundException.class, () -> updateAppointmentUseCase.execute(appointment, id));
    }

    @Test
    void shouldUpdateAppointment() {
        UUID id = UUID.randomUUID();
        Appointment appointment = getAppointment();
        when(customAppointmentRepository.findById(any(UUID.class))).thenReturn(Optional.of(appointment));
        when(customAppointmentRepository.update(any(Appointment.class))).thenReturn(appointment);
        Appointment updatedUser = updateAppointmentUseCase.execute(appointment, id);
        assertNotNull(updatedUser);
    }

    private static Appointment getAppointment() {
        Appointment appointment = new Appointment();
        appointment.setDateTime(LocalDateTime.now());
        appointment.setNotes("Updating notes...");
        appointment.setStatus(AppointmentStatus.COMPLETED);
        return appointment;
    }

}