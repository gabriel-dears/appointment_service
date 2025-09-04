package com.hospital_app.appointment_service.infra.adapter.in.controller.validation;

import com.hospital_app.appointment_service.application.exception.InvalidAppointmentCreate;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.application.port.out.user_service.UserServiceClientPort;
import com.hospital_app.appointment_service.domain.exception.DoctorNotFoundException;
import com.hospital_app.appointment_service.domain.exception.PatientNotFoundException;
import com.hospital_app.appointment_service.domain.model.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class CreateAppointmentInputValidatorImplTest {

    @Mock
    private CustomAppointmentRepository repository;
    @Mock
    private UserServiceClientPort userService;

    private CreateAppointmentInputValidatorImpl validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new CreateAppointmentInputValidatorImpl(repository, userService);
    }

    @Test
    void validate_whenDoctorInvalid_shouldThrow() {
        Appointment a = sample();
        when(userService.isDoctorValid(a.getDoctorId())).thenReturn(false);

        assertThatThrownBy(() -> validator.validate(a))
                .isInstanceOf(DoctorNotFoundException.class);
    }

    @Test
    void validate_whenPatientInvalid_shouldThrow() {
        Appointment a = sample();
        when(userService.isDoctorValid(a.getDoctorId())).thenReturn(true);
        when(userService.isPatientValid(a.getPatientId())).thenReturn(false);

        assertThatThrownBy(() -> validator.validate(a))
                .isInstanceOf(PatientNotFoundException.class);
    }

    @Test
    void validate_whenDateTimeUnavailable_shouldThrow() {
        Appointment a = sample();
        when(userService.isDoctorValid(a.getDoctorId())).thenReturn(true);
        when(userService.isPatientValid(a.getPatientId())).thenReturn(true);
        when(repository.isAppointmentTimeAvailableByDoctorOrPatientCreation(a.getPatientId(), a.getDoctorId(), a.getDateTime()))
                .thenReturn(false);

        assertThatThrownBy(() -> validator.validate(a))
                .isInstanceOf(InvalidAppointmentCreate.class);
    }

    @Test
    void validate_whenAllGood_shouldPass() {
        Appointment a = sample();
        when(userService.isDoctorValid(a.getDoctorId())).thenReturn(true);
        when(userService.isPatientValid(a.getPatientId())).thenReturn(true);
        when(repository.isAppointmentTimeAvailableByDoctorOrPatientCreation(a.getPatientId(), a.getDoctorId(), a.getDateTime()))
                .thenReturn(true);

        assertThatCode(() -> validator.validate(a)).doesNotThrowAnyException();
    }

    private Appointment sample() {
        Appointment a = new Appointment();
        a.setPatientId(UUID.randomUUID());
        a.setDoctorId(UUID.randomUUID());
        a.setDateTime(LocalDateTime.now().plusDays(1));
        return a;
    }
}
