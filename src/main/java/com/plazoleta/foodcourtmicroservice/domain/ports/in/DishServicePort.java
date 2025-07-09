package com.plazoleta.foodcourtmicroservice.domain.ports.in;

import java.math.BigDecimal;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;

public interface DishServicePort {
    void save(DishModel dishModel);

    void updateDish(Long dishId, Long restaurantId, BigDecimal price, String description);

    void setDishActive(Long dishId, Long restaurantId, boolean active);

    PageInfo<DishModel> findAllByRestaurantId(Long restaurantId, Long categoryId, Integer page, Integer size, String sortBy, boolean orderAsc);
}
