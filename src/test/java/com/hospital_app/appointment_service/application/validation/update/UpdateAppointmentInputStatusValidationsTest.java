package com.hospital_app.appointment_service.application.validation.update;

import com.hospital_app.appointment_service.application.exception.InvalidAppointmentUpdate;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.domain.model.AppointmentStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UpdateAppointmentInputStatusValidationsTest {

    @Test
    void createdStatusValidation_disallowsNoShowOrCompleted() {
        UpdateAppointmentInputValidation v = new UpdateAppointmentInputCreatedStatusValidation();
        Appointment existing = appt(AppointmentStatus.CREATED, LocalDateTime.now().plusDays(1), null);
        Appointment toNoShow = appt(AppointmentStatus.NO_SHOW, existing.getDateTime(), null);
        Appointment toCompleted = appt(AppointmentStatus.COMPLETED, existing.getDateTime(), null);
        Appointment toConfirmed = appt(AppointmentStatus.CONFIRMED, existing.getDateTime(), null);

        assertThatThrownBy(() -> v.validate(existing, toNoShow)).isInstanceOf(InvalidAppointmentUpdate.class);
        assertThatThrownBy(() -> v.validate(existing, toCompleted)).isInstanceOf(InvalidAppointmentUpdate.class);
        assertThatCode(() -> v.validate(existing, toConfirmed)).doesNotThrowAnyException();
    }

    @Test
    void confirmedStatusValidation_disallowsToCreated() {
        UpdateAppointmentInputValidation v = new UpdateAppointmentInputConfirmedStatusValidation();
        Appointment existing = appt(AppointmentStatus.CONFIRMED, LocalDateTime.now().plusDays(1), null);
        Appointment toCreated = appt(AppointmentStatus.CREATED, existing.getDateTime(), null);
        assertThatThrownBy(() -> v.validate(existing, toCreated)).isInstanceOf(InvalidAppointmentUpdate.class);
    }

    @Test
    void confirmedStatusValidation_futureDisallowsCompletedOrNoShow() {
        UpdateAppointmentInputValidation v = new UpdateAppointmentInputConfirmedStatusValidation();
        LocalDateTime future = LocalDateTime.now().plusDays(2);
        Appointment existing = appt(AppointmentStatus.CONFIRMED, future, null);
        Appointment toCompleted = appt(AppointmentStatus.COMPLETED, future, null);
        Appointment toNoShow = appt(AppointmentStatus.NO_SHOW, future, null);
        Appointment toCancelled = appt(AppointmentStatus.CANCELLED, future, null);

        assertThatThrownBy(() -> v.validate(existing, toCompleted)).isInstanceOf(InvalidAppointmentUpdate.class);
        assertThatThrownBy(() -> v.validate(existing, toNoShow)).isInstanceOf(InvalidAppointmentUpdate.class);
        assertThatCode(() -> v.validate(existing, toCancelled)).doesNotThrowAnyException();
    }

    @Test
    void cancelledStatusValidation_disallowsAnyChangeAndDateTimeProvided() {
        UpdateAppointmentInputValidation v = new UpdateAppointmentInputCancelledStatusValidation();
        Appointment existing = appt(AppointmentStatus.CANCELLED, LocalDateTime.now().minusDays(1), null);
        Appointment sameStatusButDateTimeProvided = appt(AppointmentStatus.CANCELLED, LocalDateTime.now().plusDays(1), null);
        Appointment toConfirmed = appt(AppointmentStatus.CONFIRMED, null, null);

        assertThatThrownBy(() -> v.validate(existing, sameStatusButDateTimeProvided)).isInstanceOf(InvalidAppointmentUpdate.class);
        assertThatThrownBy(() -> v.validate(existing, toConfirmed)).isInstanceOf(InvalidAppointmentUpdate.class);
        assertThatCode(() -> v.validate(existing, appt(AppointmentStatus.CANCELLED, null, null))).doesNotThrowAnyException();
    }

    @Test
    void completedStatusValidation_disallowsAnyChangeAndDateTimeProvided() {
        UpdateAppointmentInputValidation v = new UpdateAppointmentInputCompletedStatusValidation();
        Appointment existing = appt(AppointmentStatus.COMPLETED, LocalDateTime.now().minusDays(1), null);
        Appointment sameStatusButDateTimeProvided = appt(AppointmentStatus.COMPLETED, LocalDateTime.now().plusDays(1), null);
        Appointment toCancelled = appt(AppointmentStatus.CANCELLED, null, null);

        assertThatThrownBy(() -> v.validate(existing, sameStatusButDateTimeProvided)).isInstanceOf(InvalidAppointmentUpdate.class);
        assertThatThrownBy(() -> v.validate(existing, toCancelled)).isInstanceOf(InvalidAppointmentUpdate.class);
        assertThatCode(() -> v.validate(existing, appt(AppointmentStatus.COMPLETED, null, null))).doesNotThrowAnyException();
    }

    @Test
    void noShowStatusValidation_disallowsAnyChangeAndDateTimeProvided() {
        UpdateAppointmentInputValidation v = new UpdateAppointmentInputNoShowStatusValidation();
        Appointment existing = appt(AppointmentStatus.NO_SHOW, LocalDateTime.now().minusDays(1), null);
        Appointment sameStatusButDateTimeProvided = appt(AppointmentStatus.NO_SHOW, LocalDateTime.now().plusDays(1), null);
        Appointment toCancelled = appt(AppointmentStatus.CANCELLED, null, null);

        assertThatThrownBy(() -> v.validate(existing, sameStatusButDateTimeProvided)).isInstanceOf(InvalidAppointmentUpdate.class);
        assertThatThrownBy(() -> v.validate(existing, toCancelled)).isInstanceOf(InvalidAppointmentUpdate.class);
        assertThatCode(() -> v.validate(existing, appt(AppointmentStatus.NO_SHOW, null, null))).doesNotThrowAnyException();
    }

    private Appointment appt(AppointmentStatus status, LocalDateTime dateTime, String notes) {
        Appointment a = new Appointment();
        a.setStatus(status);
        a.setDateTime(dateTime);
        a.setNotes(notes);
        return a;
    }
}
