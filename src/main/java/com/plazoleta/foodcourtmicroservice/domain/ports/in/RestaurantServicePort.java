package com.plazoleta.foodcourtmicroservice.domain.ports.in;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

public interface RestaurantServicePort {

    void save(RestaurantModel restaurantModel);

}
