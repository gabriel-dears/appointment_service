package com.hospital_app.appointment_service.infra.adapter.out.user_service.grpc;

import com.hospital_app.appointment_service.application.command.UserInfo;
import com.hospital_app.appointment_service.application.exception.UserServiceException;
import com.hospital_app.appointment_service.application.port.out.user_service.UserServiceClientPort;
import com.hospital_app.proto.generated.user.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Supplier;

@Service
public class UserServiceClientGrpcAdapter implements UserServiceClientPort {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    @Override
    public boolean isPatientValid(UUID patientId) {
        UserExistsResponse userExistsResponse = userServiceGrpcInteraction(() -> userServiceBlockingStub.patientExists(UserExistsRequest.newBuilder().setUserId(patientId.toString()).build()));
        return userExistsResponse.getExists();
    }

    @Override
    public boolean isDoctorValid(UUID doctorId) {
        UserExistsResponse userExistsResponse = userServiceGrpcInteraction(() -> userServiceBlockingStub.doctorExists(UserExistsRequest.newBuilder().setUserId(doctorId.toString()).build()));
        return userExistsResponse.getExists();
    }

    @Override
    public UserInfo getUserInfo(UUID userId) {
        GetUserRequest request = GetUserRequest.newBuilder()
                .setUserId(userId.toString())
                .build();
        GetUserResponse getUserResponse = userServiceGrpcInteraction(() -> userServiceBlockingStub.getUser(request));
        return new UserInfo(
                UUID.fromString(getUserResponse.getId()),
                getUserResponse.getEmail(),
                getUserResponse.getName()
        );
    }

    private <T> T userServiceGrpcInteraction(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new UserServiceException(e.getMessage());
        }
    }

}
