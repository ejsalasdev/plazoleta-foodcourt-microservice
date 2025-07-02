package com.plazoleta.foodcourtmicroservice.domain.ports.out;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;

public interface DishPersistencePort {

    void save(DishModel dishModel);

    boolean existsByNameAndRestaurantId(String name, Long restaurantId);

}
