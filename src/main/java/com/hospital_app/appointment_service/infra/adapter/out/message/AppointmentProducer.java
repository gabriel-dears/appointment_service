package com.hospital_app.appointment_service.infra.adapter.out.message;

import com.hospital_app.appointment_service.application.port.out.message.AppointmentQueuePort;
import com.hospital_app.appointment_service.infra.config.message.rabbitmq.RabbitMQConfig;
import com.hospital_app.common.message.dto.AppointmentMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AppointmentProducer implements AppointmentQueuePort {

    private final RabbitTemplate rabbitTemplate;

    public AppointmentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendAppointment(AppointmentMessage appointmentMessage) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TRANSCRIPTION_EXCHANGE,
                "",
                appointmentMessage
        );
    }

}
