package com.plazoleta.foodcourtmicroservice.application.handler.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveDishRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.SaveDishResponse;
import com.plazoleta.foodcourtmicroservice.application.handler.DishHandler;
import com.plazoleta.foodcourtmicroservice.application.mappers.DishRequestMapper;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.DishServicePort;
import com.plazoleta.foodcourtmicroservice.application.utils.constants.ApplicationMessagesConstants;

@Service
@RequiredArgsConstructor
public class DishHandlerImpl implements DishHandler {

    private final DishRequestMapper dishRequestMapper;
    private final DishServicePort dishServicePort;

    @Override
    public SaveDishResponse save(SaveDishRequest request) {
        dishServicePort.save(dishRequestMapper.requestToModel(request));
        return new SaveDishResponse(ApplicationMessagesConstants.DISH_SAVED_SUCCESSFULLY, LocalDateTime.now());
    }
}
