package com.plazoleta.foodcourtmicroservice.application.handler;

import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveDishRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.SaveDishResponse;

public interface DishHandler {
    
    SaveDishResponse save(SaveDishRequest request);
}
