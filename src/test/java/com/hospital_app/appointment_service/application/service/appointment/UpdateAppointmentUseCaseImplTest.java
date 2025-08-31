package com.hospital_app.appointment_service.application.service.appointment;

import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentQueuePort;
import com.hospital_app.appointment_service.domain.exception.AppointmentNotFoundException;
import com.hospital_app.appointment_service.domain.exception.InvalidAppointmentDateTimeException;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAppointmentUseCaseImplTest {

    @Mock
    private CustomAppointmentRepository customAppointmentRepository;

    @Mock
    private AppointmentQueuePort appointmentQueuePort;

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
    void shouldNotUpdateAppointmentWhenDateTimeIsBeforeExistingDateTime() {
        UUID id = UUID.randomUUID();
        Appointment inputAppointment = getAppointment();
        Appointment existingAppointment = getAppointment();
        existingAppointment.setDateTime(LocalDateTime.now().plusDays(1));
        inputAppointment.setDateTime(LocalDateTime.now().minusDays(1));
        when(customAppointmentRepository.findById(any(UUID.class))).thenReturn(Optional.of(inputAppointment));
        assertThrows(InvalidAppointmentDateTimeException.class, () -> updateAppointmentUseCase.execute(inputAppointment, id));
    }

    @Test
    void shouldNotUpdateAppointmentWhenDateTimeIsEqualsToExistingDateTime() {
        UUID id = UUID.randomUUID();
        Appointment inputAppointment = getAppointment();
        Appointment existingAppointment = getAppointment();
        LocalDateTime now = LocalDateTime.now();
        existingAppointment.setDateTime(now.plusDays(1));
        inputAppointment.setDateTime(now.plusDays(1));
        when(customAppointmentRepository.findById(any(UUID.class))).thenReturn(Optional.of(inputAppointment));
        assertThrows(InvalidAppointmentDateTimeException.class, () -> updateAppointmentUseCase.execute(inputAppointment, id));
    }

    @Test
    void shouldUpdateAppointment() {
        UUID id = UUID.randomUUID();
        Appointment inputAppointment = getAppointment();
        Appointment existingAppointment = getAppointment();
        inputAppointment.setDateTime(LocalDateTime.now().plusDays(1));
        existingAppointment.setDateTime(LocalDateTime.now().minusDays(1));
        when(customAppointmentRepository.findById(any(UUID.class))).thenReturn(Optional.of(existingAppointment));
        when(customAppointmentRepository.update(any(Appointment.class))).thenReturn(existingAppointment);
        doNothing().when(appointmentQueuePort).sendAppointment(any(Appointment.class));
        Appointment updatedUser = updateAppointmentUseCase.execute(inputAppointment, id);
        assertNotNull(updatedUser);
    }

    private static Appointment getAppointment() {
        Appointment appointment = new Appointment();
        appointment.setNotes("Updating notes...");
        appointment.setStatus(AppointmentStatus.COMPLETED);
        return appointment;
    }

}