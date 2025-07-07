package com.plazoleta.foodcourtmicroservice.infrastructure.adapters.external;

import com.plazoleta.foodcourtmicroservice.application.client.handler.UserHandlerClient;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.UserServicePort;
import com.plazoleta.foodcourtmicroservice.infrastructure.exceptions.UserNotFoundException;
import com.plazoleta.foodcourtmicroservice.infrastructure.utils.constants.InfrastructureMessagesConstants;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceAdapter implements UserServicePort {
    private final UserHandlerClient userHandlerClient;

    @Override
    public String getUserRoleById(Long userId) {
        try {
            return userHandlerClient.getUserInfobyId(userId).role();
        } catch (FeignException.NotFound e) {
            throw new UserNotFoundException(
                    String.format(InfrastructureMessagesConstants.USER_NOT_FOUND, userId));
        }
    }
}
