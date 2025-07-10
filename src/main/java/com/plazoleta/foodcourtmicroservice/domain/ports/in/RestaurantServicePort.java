package com.plazoleta.foodcourtmicroservice.domain.ports.in;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;

public interface RestaurantServicePort {

    void save(RestaurantModel restaurantModel);

    PageInfo<RestaurantModel> findAll(Integer page, Integer size, String sortBy, boolean orderAsc);

    RestaurantModel findByOwnerId(Long ownerId);

}
