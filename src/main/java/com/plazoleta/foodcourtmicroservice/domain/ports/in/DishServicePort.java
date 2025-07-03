package com.plazoleta.foodcourtmicroservice.domain.ports.in;

import java.math.BigDecimal;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;

public interface DishServicePort {
    void save(DishModel dishModel);

    void updateDish(Long dishId, Long restaurantId, BigDecimal price, String description);
}
