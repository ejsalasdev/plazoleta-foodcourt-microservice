package com.plazoleta.foodcourtmicroservice.domain.ports.out;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

public interface RestaurantPersistencePort {

    void save(RestaurantModel restaurant);

    boolean existsByNit(String nit);

}
