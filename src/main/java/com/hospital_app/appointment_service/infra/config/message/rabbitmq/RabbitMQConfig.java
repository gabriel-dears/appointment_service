package com.hospital_app.appointment_service.infra.config.message.rabbitmq;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TRANSCRIPTION_EXCHANGE = "appointment-exchange";

    @Bean
    public FanoutExchange transcriptionExchange() {
        return new FanoutExchange(TRANSCRIPTION_EXCHANGE);
    }

}
