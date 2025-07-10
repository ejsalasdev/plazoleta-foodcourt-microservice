package com.plazoleta.foodcourtmicroservice.application.dto.response;

public record OrderDishResponse(
        Long id,
        DishResponse dish,
        Integer quantity
) {
}
