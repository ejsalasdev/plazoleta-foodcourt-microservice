package com.plazoleta.foodcourtmicroservice.application.handler.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveRestaurantRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.RestaurantResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.SaveRestaurantResponse;
import com.plazoleta.foodcourtmicroservice.application.handler.RestaurantHandler;
import com.plazoleta.foodcourtmicroservice.application.mappers.RestaurantRequestMapper;
import com.plazoleta.foodcourtmicroservice.application.mappers.RestaurantResponseMapper;
import com.plazoleta.foodcourtmicroservice.application.utils.constants.ApplicationMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.RestaurantServicePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantHandlerImpl implements RestaurantHandler {

    private final RestaurantRequestMapper restaurantRequestMapper;
    private final RestaurantServicePort restaurantServicePort;
    private final RestaurantResponseMapper restaurantResponseMapper;

    @Override
    public SaveRestaurantResponse save(SaveRestaurantRequest request) {
        restaurantServicePort.save(restaurantRequestMapper.requestToModel(request));
        return new SaveRestaurantResponse(ApplicationMessagesConstants.RESTAURANT_SAVED_SUCCESSFULLY, LocalDateTime.now());
    }

    @Override
    public PageInfo<RestaurantResponse> findAll(Integer page, Integer size, String sortBy, boolean orderAsc) {
        PageInfo<RestaurantModel> restaurantPageInfo = restaurantServicePort.findAll(page, size, sortBy, orderAsc);
        List<RestaurantResponse> restaurantResponses = restaurantPageInfo.getContent().stream()
                .map(restaurantResponseMapper::modelToResponse)
                .toList();
        return new PageInfo<>(
                restaurantResponses,
                restaurantPageInfo.getTotalElements(),
                restaurantPageInfo.getTotalPages(),
                restaurantPageInfo.getCurrentPage(),
                restaurantPageInfo.getPageSize(),
                restaurantPageInfo.isHasNext(),
                restaurantPageInfo.isHasPrevious()
        );
    }
}
