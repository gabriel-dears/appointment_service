package com.hospital_app.appointment_service.infra.config.appointment;

import com.hospital_app.appointment_service.application.port.in.appointment.CreateAppointmentUseCase;
import com.hospital_app.appointment_service.application.port.in.appointment.UpdateAppointmentUseCase;
import com.hospital_app.appointment_service.application.port.out.db.CustomAppointmentRepository;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentMessageComposerPort;
import com.hospital_app.appointment_service.application.port.out.message.AppointmentQueuePort;
import com.hospital_app.appointment_service.application.port.out.user_service.UserServiceClientPort;
import com.hospital_app.appointment_service.application.service.appointment.CreateAppointmentUseCaseImpl;
import com.hospital_app.appointment_service.application.service.appointment.UpdateAppointmentUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppointmentBeanConfig {

    @Bean
    CreateAppointmentUseCase createAppointmentUseCase(CustomAppointmentRepository customAppointmentRepository, UserServiceClientPort userServiceClientPort, AppointmentQueuePort appointmentQueuePort, AppointmentMessageComposerPort appointmentMessageComposerPort) {
        return new CreateAppointmentUseCaseImpl(customAppointmentRepository, userServiceClientPort, appointmentQueuePort, appointmentMessageComposerPort);
    }

    @Bean
    UpdateAppointmentUseCase updateAppointmentUseCase(CustomAppointmentRepository customAppointmentRepository, AppointmentQueuePort appointmentQueuePort, AppointmentMessageComposerPort appointmentMessageComposerPort) {
        return new UpdateAppointmentUseCaseImpl(customAppointmentRepository, appointmentQueuePort, appointmentMessageComposerPort);
    }

}
