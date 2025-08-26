package com.hospital_app.appointment_service.application.port.out.user_service;

public interface UserServiceClientPort {

    boolean isPatientValid(Long patientId);

    boolean isDoctorValid(Long doctorId);

}
