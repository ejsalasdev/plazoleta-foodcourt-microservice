package com.plazoleta.foodcourtmicroservice.application.dto.request;

import java.math.BigDecimal;

public record UpdateDishRequest(
        Long restaurantId,
        BigDecimal price,
        String description) {
}
