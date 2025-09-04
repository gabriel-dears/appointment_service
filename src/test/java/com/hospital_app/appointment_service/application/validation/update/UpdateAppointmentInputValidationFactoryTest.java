package com.hospital_app.appointment_service.application.validation.update;

import com.hospital_app.appointment_service.domain.model.AppointmentStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateAppointmentInputValidationFactoryTest {

    @Test
    void getValidation_returnsCorrectImplementationForEachStatus() {
        assertThat(UpdateAppointmentInputValidationFactory.getValidation(AppointmentStatus.CREATED))
                .isInstanceOf(UpdateAppointmentInputCreatedStatusValidation.class);
        assertThat(UpdateAppointmentInputValidationFactory.getValidation(AppointmentStatus.CONFIRMED))
                .isInstanceOf(UpdateAppointmentInputConfirmedStatusValidation.class);
        assertThat(UpdateAppointmentInputValidationFactory.getValidation(AppointmentStatus.NO_SHOW))
                .isInstanceOf(UpdateAppointmentInputNoShowStatusValidation.class);
        assertThat(UpdateAppointmentInputValidationFactory.getValidation(AppointmentStatus.CANCELLED))
                .isInstanceOf(UpdateAppointmentInputCancelledStatusValidation.class);
        assertThat(UpdateAppointmentInputValidationFactory.getValidation(AppointmentStatus.COMPLETED))
                .isInstanceOf(UpdateAppointmentInputCompletedStatusValidation.class);
    }
}
