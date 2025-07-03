package com.plazoleta.foodcourtmicroservice.domain.ports.out;

import java.math.BigDecimal;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;

public interface DishPersistencePort {

    void save(DishModel dishModel);

    boolean existsByNameAndRestaurantId(String name, Long restaurantId);

    void updateDish(Long dishId, Long restaurantId, BigDecimal price, String description);
}
