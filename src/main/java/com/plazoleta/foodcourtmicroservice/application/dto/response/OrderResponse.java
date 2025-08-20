package com.plazoleta.foodcourtmicroservice.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderResponse(
                @Schema(description = "Order ID", example = "1") Long id,

                @Schema(description = "Customer ID", example = "123") Long customerId,

                @Schema(description = "Order creation date", example = "2025-07-09T22:30:00") LocalDateTime date,

                @Schema(description = "Order status", example = "PENDING") OrderStatusEnum status,

                @Schema(description = "Employee ID assigned to the order", example = "null") Long employeeId,

                @Schema(description = "Restaurant information") RestaurantResponse restaurant,

                @Schema(description = "List of dishes in the order") List<OrderDishResponse> orderDishes) {
}
