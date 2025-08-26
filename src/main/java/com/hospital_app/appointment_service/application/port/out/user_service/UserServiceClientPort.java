package com.hospital_app.appointment_service.application.port.out.user_service;

import java.util.UUID;

public interface UserServiceClientPort {

    boolean isPatientValid(UUID patientId);

    boolean isDoctorValid(UUID doctorId);

}
