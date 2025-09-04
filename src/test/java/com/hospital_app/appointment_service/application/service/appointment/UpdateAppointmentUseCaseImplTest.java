package com.hospital_app.appointment_service.application.service.appointment;

import com.hospital_app.appointment_service.application.mapper.AppointmentMapper;
import com.hospital_app.appointment_service.application.port.in.validator.UpdateAppointmentInputValidatorPort;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentMessageComposerPort;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentQueuePort;
import com.hospital_app.appointment_service.domain.exception.AppointmentNotFoundException;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.domain.model.AppointmentStatus;
import com.hospital_app.common.message.dto.AppointmentMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class UpdateAppointmentUseCaseImplTest {

    @Mock
    private CustomAppointmentRepository repository;
    @Mock
    private AppointmentQueuePort queuePort;
    @Mock
    private AppointmentMessageComposerPort composer;
    @Mock
    private UpdateAppointmentInputValidatorPort validator;

    private UpdateAppointmentUseCaseImpl useCase;

    private UUID existingId;
    private Appointment existing;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new UpdateAppointmentUseCaseImpl(repository, queuePort, composer, validator);
        existingId = UUID.randomUUID();
        existing = buildAppointment(existingId, AppointmentStatus.CREATED, "notes", LocalDateTime.now().plusDays(1));
    }

    @Test
    void execute_whenNoChanges_shouldReturnExistingWithoutInteractions() {
        when(repository.findById(existingId)).thenReturn(Optional.of(copy(existing)));

        Appointment input = buildAppointment(null, existing.getStatus(), existing.getNotes(), existing.getDateTime());

        Appointment result = useCase.execute(input, existingId);

        assertThat(result.getId()).isEqualTo(existingId);
        verify(repository, times(1)).findById(existingId);
        verifyNoInteractions(validator, composer, queuePort);
        verify(repository, never()).update(any());
    }

    @Test
    void execute_whenStatusChange_shouldValidateUpdateAndSend() {
        when(repository.findById(existingId)).thenReturn(Optional.of(copy(existing)));
        Appointment input = buildAppointment(null, AppointmentStatus.CONFIRMED, existing.getNotes(), existing.getDateTime());
        Appointment mapped = AppointmentMapper.fromInputToExistingAppointmentForUpdate(input, copy(existing));
        Appointment updated = copy(mapped);
        AppointmentMessage message = new AppointmentMessage();

        when(repository.update(any())).thenReturn(updated);
        when(composer.compose(updated)).thenReturn(message);

        Appointment result = useCase.execute(input, existingId);

        verify(validator, times(1)).validate(any(Appointment.class), eq(input));
        verify(repository, times(1)).update(any(Appointment.class));
        verify(queuePort, times(1)).sendAppointment(message);
        assertThat(result).isSameAs(updated);
    }

    @Test
    void execute_whenNotesChanged_shouldValidateUpdateAndSend() {
        when(repository.findById(existingId)).thenReturn(Optional.of(copy(existing)));
        Appointment input = buildAppointment(null, existing.getStatus(), "new-notes", existing.getDateTime());
        Appointment updated = copy(existing);
        updated.setNotes("new-notes");
        AppointmentMessage message = new AppointmentMessage();

        when(repository.update(any())).thenReturn(updated);
        when(composer.compose(updated)).thenReturn(message);

        Appointment result = useCase.execute(input, existingId);

        verify(validator, times(1)).validate(any(Appointment.class), eq(input));
        verify(repository, times(1)).update(any(Appointment.class));
        verify(queuePort, times(1)).sendAppointment(message);
        assertThat(result).isSameAs(updated);
    }

    @Test
    void execute_whenDateChangedNonNull_shouldValidateUpdateAndSend() {
        when(repository.findById(existingId)).thenReturn(Optional.of(copy(existing)));
        Appointment input = buildAppointment(null, existing.getStatus(), existing.getNotes(), existing.getDateTime().plusDays(2));
        Appointment updated = copy(existing);
        updated.setDateTime(input.getDateTime());
        AppointmentMessage message = new AppointmentMessage();

        when(repository.update(any())).thenReturn(updated);
        when(composer.compose(updated)).thenReturn(message);

        Appointment result = useCase.execute(input, existingId);

        verify(validator, times(1)).validate(any(Appointment.class), eq(input));
        verify(repository, times(1)).update(any(Appointment.class));
        verify(queuePort, times(1)).sendAppointment(message);
        assertThat(result).isSameAs(updated);
    }

    @Test
    void execute_whenNotFound_shouldThrow() {
        when(repository.findById(existingId)).thenReturn(Optional.empty());
        Appointment input = buildAppointment(null, AppointmentStatus.CONFIRMED, "x", LocalDateTime.now().plusDays(1));

        assertThatThrownBy(() -> useCase.execute(input, existingId))
                .isInstanceOf(AppointmentNotFoundException.class)
                .hasMessageContaining("Appointment with id");

    }

    private Appointment buildAppointment(UUID id, AppointmentStatus status, String notes, LocalDateTime dateTime) {
        Appointment a = new Appointment();
        a.setId(id);
        a.setPatientId(UUID.randomUUID());
        a.setDoctorId(UUID.randomUUID());
        a.setDateTime(dateTime);
        a.setStatus(status);
        a.setNotes(notes);
        return a;
    }

    private Appointment copy(Appointment src) {
        Appointment a = new Appointment();
        a.setId(src.getId());
        a.setPatientId(src.getPatientId());
        a.setDoctorId(src.getDoctorId());
        a.setDateTime(src.getDateTime());
        a.setStatus(src.getStatus());
        a.setNotes(src.getNotes());
        return a;
    }
}
