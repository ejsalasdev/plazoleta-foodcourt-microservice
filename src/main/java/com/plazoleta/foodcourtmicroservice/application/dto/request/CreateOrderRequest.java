package com.plazoleta.foodcourtmicroservice.application.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateOrderRequest(

                @Schema(description = "ID of the restaurant for the order", example = "1") Long restaurantId,

                @Schema(description = "List of dishes in the order with their quantities") List<OrderDishRequest> dishes) {
}