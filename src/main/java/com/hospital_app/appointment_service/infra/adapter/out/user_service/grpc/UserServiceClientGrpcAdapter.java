package com.hospital_app.appointment_service.infra.adapter.out.user_service.grpc;

import com.hospital_app.appointment_service.application.command.UserInfo;
import com.hospital_app.appointment_service.application.exception.UserServiceException;
import com.hospital_app.appointment_service.application.port.out.user_service.UserServiceClientPort;
import com.hospital_app.proto.generated.user.*;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class UserServiceClientGrpcAdapter implements UserServiceClientPort {

    @Override
    public boolean isPatientValid(UUID patientId) {
        UserExistsResponse userExistsResponse = userServiceGrpcInteraction(() -> getUserServiceBlockingStub().patientExists(UserExistsRequest.newBuilder().setUserId(patientId.toString()).build()));
        return userExistsResponse.getExists();
    }

    @Override
    public boolean isDoctorValid(UUID doctorId) {
        UserExistsResponse userExistsResponse = userServiceGrpcInteraction(() -> getUserServiceBlockingStub().doctorExists(UserExistsRequest.newBuilder().setUserId(doctorId.toString()).build()));
        return userExistsResponse.getExists();
    }

    @Override
    public UserInfo getUserInfo(UUID userId) {
        GetUserRequest request = GetUserRequest.newBuilder()
                .setUserId(userId.toString())
                .build();
        GetUserResponse getUserResponse = userServiceGrpcInteraction(() -> getUserServiceBlockingStub().getUser(request));
        return new UserInfo(
                UUID.fromString(getUserResponse.getId()),
                getUserResponse.getEmail(),
                getUserResponse.getName()
        );
    }

    private UserServiceGrpc.UserServiceBlockingStub getUserServiceBlockingStub() {
        try {
            ManagedChannel channel = NettyChannelBuilder.forAddress("user-service", 9090)
                    .sslContext(GrpcSslContexts.forClient()
                            .trustManager(new File("tls/ca.crt"))
                            .keyManager(new File("tls/appointment_service.crt"), new File("tls/appointment_service_pkcs8.key"))
                            .build())
                    .build();
            return UserServiceGrpc.newBlockingStub(channel)
                    ;
        } catch (Exception e) {
            throw new UserServiceException(e.getMessage());
        }
    }

    private <T> T userServiceGrpcInteraction(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new UserServiceException(e.getMessage());
        }
    }

}
