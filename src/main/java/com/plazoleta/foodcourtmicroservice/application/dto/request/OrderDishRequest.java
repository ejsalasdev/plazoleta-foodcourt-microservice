package com.plazoleta.foodcourtmicroservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderDishRequest(

                @Schema(description = "ID of the dish to order", example = "1") Long dishId,

                @Schema(description = "Quantity of the dish", example = "2") Integer quantity) {
}
