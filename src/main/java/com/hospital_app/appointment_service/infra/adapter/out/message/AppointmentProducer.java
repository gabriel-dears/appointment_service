package com.hospital_app.appointment_service.infra.adapter.out.message;

import com.hospital_app.appointment_service.application.port.out.message.AppointmentQueuePort;
import com.hospital_app.appointment_service.domain.model.Appointment;
import com.hospital_app.appointment_service.infra.config.message.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AppointmentProducer implements AppointmentQueuePort {

    private final RabbitTemplate rabbitTemplate;

    public AppointmentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendAppointment(Appointment appointment) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TRANSCRIPTION_EXCHANGE,
                "",
                appointment
        );
    }

}
