package com.plazoleta.foodcourtmicroservice.application.handler.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveDishRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.request.SetDishActiveRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.request.UpdateDishRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.DishResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.SaveDishResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.UpdateDishResponse;
import com.plazoleta.foodcourtmicroservice.application.handler.DishHandler;
import com.plazoleta.foodcourtmicroservice.application.mappers.DishRequestMapper;
import com.plazoleta.foodcourtmicroservice.application.mappers.DishResponseMapper;
import com.plazoleta.foodcourtmicroservice.application.utils.constants.ApplicationMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.DishServicePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DishHandlerImpl implements DishHandler {

    private final DishRequestMapper dishRequestMapper;
    private final DishServicePort dishServicePort;
    private final DishResponseMapper dishResponseMapper;

    @Override
    public SaveDishResponse save(SaveDishRequest request) {
        dishServicePort.save(dishRequestMapper.requestToModel(request));
        return new SaveDishResponse(ApplicationMessagesConstants.DISH_SAVED_SUCCESSFULLY, LocalDateTime.now());
    }

    @Override
    public UpdateDishResponse update(Long dishId, UpdateDishRequest request) {
        dishServicePort.updateDish(dishId, request.restaurantId(), request.price(), request.description());
        return new UpdateDishResponse(ApplicationMessagesConstants.DISH_UPDATED_SUCCESSFULLY, LocalDateTime.now());
    }

    @Override
    public void setActive(Long dishId, SetDishActiveRequest request) {
        dishServicePort.setDishActive(dishId, request.getRestaurantId(), request.getActive());
    }

    @Override
    public PageInfo<DishResponse> findAllByRestaurantId(Long restaurantId, Long categoryId, Integer page, Integer size,
            String sortBy, boolean orderAsc) {
        PageInfo<DishModel> dishPageInfo = dishServicePort.findAllByRestaurantId(restaurantId, categoryId, page, size,
                sortBy, orderAsc);
        List<DishResponse> dishResponses = dishPageInfo.getContent().stream()
                .map(dishResponseMapper::modelToResponse)
                .toList();
        return new PageInfo<>(
                dishResponses,
                dishPageInfo.getTotalElements(),
                dishPageInfo.getTotalPages(),
                dishPageInfo.getCurrentPage(),
                dishPageInfo.getPageSize(),
                dishPageInfo.isHasNext(),
                dishPageInfo.isHasPrevious());
    }
}
