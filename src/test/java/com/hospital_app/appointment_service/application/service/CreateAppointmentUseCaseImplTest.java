package com.hospital_app.appointment_service.application.service;

import com.hospital_app.appointment_service.application.exception.UserServiceException;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.application.port.out.user_service.UserServiceClientPort;
import com.hospital_app.appointment_service.domain.exception.DoctorNotFoundException;
import com.hospital_app.appointment_service.domain.exception.PatientNotFoundException;
import com.hospital_app.appointment_service.domain.model.Appointment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAppointmentUseCaseImplTest {

    @Mock
    private CustomAppointmentRepository customAppointmentRepository;

    @Mock
    private UserServiceClientPort userServiceClientPort;

    @InjectMocks
    private CreateAppointmentUseCaseImpl createAppointmentUseCase;

    @Test
    void shouldNotCreateAppointmentWhenDoctorIdDoesntExist() {
        var appointment = getAppointment();
        doThrow(DoctorNotFoundException.class).when(userServiceClientPort).isDoctorValid(any(UUID.class));
        assertThrows(DoctorNotFoundException.class, () -> createAppointmentUseCase.execute(appointment));
    }

    private static Appointment getAppointment() {
        var appointment = new Appointment();
        appointment.setDoctorId(UUID.randomUUID());
        appointment.setPatientId(UUID.randomUUID());
        return appointment;
    }

    @Test
    void shouldNotCreateAppointmentWhenPatientIdDoesntExist() {
        var appointment = getAppointment();
        when(userServiceClientPort.isDoctorValid(any(UUID.class))).thenReturn(true);
        doThrow(PatientNotFoundException.class).when(userServiceClientPort).isPatientValid(any(UUID.class));
        assertThrows(PatientNotFoundException.class, () -> createAppointmentUseCase.execute(appointment));
    }

    @Test
    void shouldNotCreateAppointmentWhenUserServiceThrowsException() {
        var appointment = getAppointment();
        doThrow(UserServiceException.class).when(userServiceClientPort).isDoctorValid(any(UUID.class));
        assertThrows(UserServiceException.class, () -> createAppointmentUseCase.execute(appointment));
    }

    @Test
    void shouldCreateAppointmentWhenEverythingIsOk() {
        var appointment = getAppointment();
        when(userServiceClientPort.isDoctorValid(any(UUID.class))).thenReturn(true);
        when(userServiceClientPort.isPatientValid(any(UUID.class))).thenReturn(true);
        when(customAppointmentRepository.create(any(Appointment.class))).thenReturn(appointment);
        Appointment createdAppointment = createAppointmentUseCase.execute(appointment);
        assertNotNull(createdAppointment);
    }

}