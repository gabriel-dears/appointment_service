package com.hospital_app.appointment_service.application.command;

import java.util.UUID;

public record UserInfo(
        UUID id,
        String email,
        String name
) {
}
