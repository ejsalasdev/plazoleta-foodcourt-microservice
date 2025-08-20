package com.plazoleta.foodcourtmicroservice.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderDishResponse(
                @Schema(description = "Order dish ID", example = "1") Long id,

                @Schema(description = "Dish information") DishResponse dish,

                @Schema(description = "Quantity ordered", example = "2") Integer quantity) {
}
