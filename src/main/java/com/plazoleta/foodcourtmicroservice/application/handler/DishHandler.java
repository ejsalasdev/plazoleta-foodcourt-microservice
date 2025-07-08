package com.plazoleta.foodcourtmicroservice.application.handler;

import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveDishRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.request.UpdateDishRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.SaveDishResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.UpdateDishResponse;

import com.plazoleta.foodcourtmicroservice.application.dto.request.SetDishActiveRequest;

public interface DishHandler {
    SaveDishResponse save(SaveDishRequest request);

    UpdateDishResponse update(Long dishId, UpdateDishRequest request);
    
    void setActive(Long dishId, SetDishActiveRequest request);
}
