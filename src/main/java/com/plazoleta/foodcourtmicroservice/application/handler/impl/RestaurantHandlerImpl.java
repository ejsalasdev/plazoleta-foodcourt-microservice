package com.plazoleta.foodcourtmicroservice.application.handler.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveRestaurantRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.SaveRestaurantResponse;
import com.plazoleta.foodcourtmicroservice.application.handler.RestaurantHandler;
import com.plazoleta.foodcourtmicroservice.application.mappers.RestaurantRequestMapper;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.RestaurantServicePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantHandlerImpl implements RestaurantHandler {

    private final RestaurantRequestMapper restaurantRequestMapper;
    private final RestaurantServicePort restaurantServicePort;

    @Override
    public SaveRestaurantResponse save(SaveRestaurantRequest request) {
        restaurantServicePort.save(restaurantRequestMapper.requestToModel(request));
        return new SaveRestaurantResponse("Restaurant saved successfully", LocalDateTime.now());
    }

}
