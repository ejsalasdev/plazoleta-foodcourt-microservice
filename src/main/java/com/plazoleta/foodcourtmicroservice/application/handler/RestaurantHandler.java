package com.plazoleta.foodcourtmicroservice.application.handler;

import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveRestaurantRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.RestaurantResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.SaveRestaurantResponse;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;

public interface RestaurantHandler {

    SaveRestaurantResponse save(SaveRestaurantRequest request);

    PageInfo<RestaurantResponse> findAll(Integer page, Integer size, String sortBy, boolean orderAsc);

}
