package com.plazoleta.foodcourtmicroservice.infrastructure.adapters.external;

import com.plazoleta.foodcourtmicroservice.application.client.handler.UserHandlerClient;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.UserServicePort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceAdapter implements UserServicePort {
    private final UserHandlerClient userHandlerClient;

    @Override
    public String getUserRoleById(Long userId) {
        return userHandlerClient.getUserInfobyId(userId).role();
    }
}
