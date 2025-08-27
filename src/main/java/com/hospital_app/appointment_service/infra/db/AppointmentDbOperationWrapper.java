package com.hospital_app.appointment_service.infra.db;

import com.hospital_app.appointment_service.application.exception.AppointmentDbException;
import com.hospital_app.common.db.DbOperationWrapper;

public class AppointmentDbOperationWrapper {

    public static <T> T execute(DbOperationWrapper.DbOperation<T> operation) {
        return DbOperationWrapper.execute(operation, AppointmentDbException.class);
    }

}
