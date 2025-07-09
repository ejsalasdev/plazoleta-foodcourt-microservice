package com.plazoleta.foodcourtmicroservice.domain.ports.out;

import java.math.BigDecimal;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;

public interface DishPersistencePort {

    void save(DishModel dishModel);

    boolean existsByNameAndRestaurantId(String name, Long restaurantId);

    void updateDish(Long dishId, Long restaurantId, BigDecimal price, String description);

    boolean existsByIdAndRestaurantId(Long dishId, Long restaurantId);

    void setDishActive(Long dishId, Long restaurantId, boolean active);

    boolean existsByRestaurantIdAndOwnerId(Long restaurantId, Long currentUserId);
}
