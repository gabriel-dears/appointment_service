package com.hospital_app.appointment_service.infra.adapter.out.user_service.rest;

import com.hospital_app.appointment_service.application.command.UserInfo;
import com.hospital_app.appointment_service.application.exception.UserServiceException;
import com.hospital_app.appointment_service.application.port.out.user_service.UserServiceClientPort;
import com.hospital_app.appointment_service.infra.adapter.out.user_service.rest.dto.UserExistsDto;
import com.hospital_app.appointment_service.infra.service.TokenExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Deprecated
public class UserServiceClientRestAdapter implements UserServiceClientPort {

    private final RestTemplate restTemplate;
    private final String userServiceUrl;
    private final TokenExtractor tokenExtractor;

    public UserServiceClientRestAdapter(RestTemplate restTemplate, @Value("${user-service-url}") String userServiceUrl, TokenExtractor tokenExtractor) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public boolean isPatientValid(UUID patientId) {
        return userExists(patientId, "patient");
    }

    @Override
    public boolean isDoctorValid(UUID doctorId) {
        return userExists(doctorId, "doctor");
    }

    @Override
    public UserInfo getUserInfo(UUID userId) {
        return null;
    }


    private boolean userExists(UUID userId, String resource) throws UserServiceException {
        String url = String.format("%s/%s/%s/exists", userServiceUrl, resource, userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenExtractor.getCurrentJwtToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<UserExistsDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, UserExistsDto.class);
            return response.getBody() != null && response.getBody().exists();
        } catch (Exception e) {
            throw new UserServiceException("Error while interacting with user service");
        }
    }
}
