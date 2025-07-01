package com.plazoleta.foodcourtmicroservice.application.handler;

import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveRestaurantRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.SaveRestaurantResponse;

public interface RestaurantHandler {

    SaveRestaurantResponse save(SaveRestaurantRequest request);

}
