package com.plazoleta.foodcourtmicroservice.application.dto.response;

import java.math.BigDecimal;

public record DishResponse(
    Long id,
    String name,
    BigDecimal price,
    String description,
    String urlImage,
    Long categoryId,
    String restaurantName
) {
}