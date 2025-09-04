package com.hospital_app.appointment_service.application.service.appointment;

import com.hospital_app.appointment_service.application.port.in.validator.CreateAppointmentInputValidatorPort;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentMessageComposerPort;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentQueuePort;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.common.message.dto.AppointmentMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CreateAppointmentUseCaseImplTest {

    @Mock
    private CustomAppointmentRepository repository;
    @Mock
    private AppointmentQueuePort queuePort;
    @Mock
    private AppointmentMessageComposerPort composer;
    @Mock
    private CreateAppointmentInputValidatorPort validator;

    private CreateAppointmentUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateAppointmentUseCaseImpl(repository, queuePort, composer, validator);
    }

    @Test
    void execute_shouldValidatePersistComposeAndSend() {
        Appointment input = buildAppointment(null);
        Appointment persisted = buildAppointment(UUID.randomUUID());
        AppointmentMessage message = new AppointmentMessage();

        when(repository.create(input)).thenReturn(persisted);
        when(composer.compose(persisted)).thenReturn(message);

        Appointment result = useCase.execute(input);

        // Validate interactions order/occurrence
        verify(validator, times(1)).validate(input);
        verify(repository, times(1)).create(input);
        verify(composer, times(1)).compose(persisted);
        verify(queuePort, times(1)).sendAppointment(message);
        verifyNoMoreInteractions(validator, repository, composer, queuePort);

        // Validate result
        assertThat(result).isSameAs(persisted);
    }

    private Appointment buildAppointment(UUID id) {
        Appointment a = new Appointment();
        a.setId(id);
        a.setPatientId(UUID.randomUUID());
        a.setDoctorId(UUID.randomUUID());
        a.setDateTime(LocalDateTime.now().plusDays(1));
        a.setNotes("Initial");
        return a;
    }
}
