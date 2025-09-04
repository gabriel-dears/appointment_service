package com.hospital_app.appointment_service.infra.adapter.in.controller.validation;

import com.hospital_app.appointment_service.application.exception.InvalidAppointmentUpdate;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.domain.model.AppointmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class UpdateAppointmentInputValidatorImplTest {

    @Mock
    private CustomAppointmentRepository repository;

    private UpdateAppointmentInputValidatorImpl validator;

    private Appointment existing;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new UpdateAppointmentInputValidatorImpl(repository);
        existing = sampleExisting(AppointmentStatus.CREATED);
    }

    @Test
    void validate_whenStatusSameAndNoNotesOrDateTime_shouldThrow() {
        Appointment input = new Appointment();
        input.setStatus(existing.getStatus());
        input.setNotes(null);
        input.setDateTime(null);

        assertThatThrownBy(() -> validator.validate(existing, input))
                .isInstanceOf(InvalidAppointmentUpdate.class)
                .hasMessageContaining("either 'notes' or the appointment 'dateTime' must be provided");
    }

    @Test
    void validate_whenStatusChanges_shouldNotCheckRepositoryAndPassForValidStatusChange() {
        Appointment input = new Appointment();
        input.setStatus(AppointmentStatus.CONFIRMED); // from CREATED -> CONFIRMED allowed by status validation
        input.setNotes(null);
        input.setDateTime(null);

        assertThatCode(() -> validator.validate(existing, input)).doesNotThrowAnyException();
        verify(repository, never()).isAppointmentTimeAvailableByDoctorOrPatientUpdate(any(), any(), any(), any());
    }

    @Test
    void validate_whenDateTimeProvidedAndUnavailable_shouldThrow() {
        LocalDateTime newTime = LocalDateTime.now().plusDays(2);
        Appointment input = new Appointment();
        input.setStatus(existing.getStatus());
        input.setNotes("some"); // to bypass first check
        input.setDateTime(newTime);

        when(repository.isAppointmentTimeAvailableByDoctorOrPatientUpdate(existing.getPatientId(), existing.getDoctorId(), existing.getId(), newTime))
                .thenReturn(false);

        assertThatThrownBy(() -> validator.validate(existing, input))
                .isInstanceOf(InvalidAppointmentUpdate.class)
                .hasMessageContaining("requested appointment time is unavailable");
    }

    @Test
    void validate_whenDateTimeProvidedAndAvailable_shouldPass() {
        LocalDateTime newTime = LocalDateTime.now().plusDays(2);
        Appointment input = new Appointment();
        input.setStatus(existing.getStatus());
        input.setNotes("some");
        input.setDateTime(newTime);

        when(repository.isAppointmentTimeAvailableByDoctorOrPatientUpdate(existing.getPatientId(), existing.getDoctorId(), existing.getId(), newTime))
                .thenReturn(true);

        assertThatCode(() -> validator.validate(existing, input)).doesNotThrowAnyException();
    }

    private Appointment sampleExisting(AppointmentStatus status) {
        Appointment a = new Appointment();
        a.setId(UUID.randomUUID());
        a.setPatientId(UUID.randomUUID());
        a.setDoctorId(UUID.randomUUID());
        a.setStatus(status);
        a.setDateTime(LocalDateTime.now().plusDays(1));
        a.setNotes("old");
        return a;
    }
}
