package com.hospital_app.appointment_service.infra.config.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8000/appointment-service")
                                .description("Kong Gateway")
                ))
                .info(new Info().title("Appointment Service API")
                        .description("API for managing appointments")
                        .version("1.0"));
    }

}
