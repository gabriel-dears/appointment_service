package com.hospital_app.appointment_service.application.validation;

import com.hospital_app.appointment_service.domain.model.AppointmentStatus;

import java.util.HashMap;
import java.util.Map;

public class UpdateAppointmentInputValidationFactory {

    private static final Map<AppointmentStatus, UpdateAppointmentInputValidation> VALIDATION_MAP = new HashMap<>();

    static {

        VALIDATION_MAP.put(AppointmentStatus.CREATED, new UpdateAppointmentInputConfirmedStatusValidation());
        VALIDATION_MAP.put(AppointmentStatus.CONFIRMED, new UpdateAppointmentInputCreatedStatusValidation());
        VALIDATION_MAP.put(AppointmentStatus.NO_SHOW, new UpdateAppointmentInputNoShowStatusValidation());
        VALIDATION_MAP.put(AppointmentStatus.CANCELLED, new UpdateAppointmentInputCancelledStatusValidation());
        VALIDATION_MAP.put(AppointmentStatus.COMPLETED, new UpdateAppointmentInputCompletedStatusValidation());

    }

    public static UpdateAppointmentInputValidation getValidation(AppointmentStatus status) {
        return VALIDATION_MAP.get(status);
    }

}
