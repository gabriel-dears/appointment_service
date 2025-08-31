package com.hospital_app.appointment_service.application.port.out.user_service;

import com.hospital_app.appointment_service.application.command.UserInfo;

import java.util.Optional;
import java.util.UUID;

public interface UserServiceClientPort {

    boolean isPatientValid(UUID patientId);

    boolean isDoctorValid(UUID doctorId);

    UserInfo getUserInfo(UUID userId);

}
