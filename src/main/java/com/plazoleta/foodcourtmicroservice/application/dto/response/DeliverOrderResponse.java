package com.plazoleta.foodcourtmicroservice.application.dto.response;

import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for delivering an order")
public record DeliverOrderResponse(
        @Schema(description = "Order ID", example = "1")
        Long id,
        
        @Schema(description = "Updated order status", example = "DELIVERED")
        OrderStatusEnum status,
        
        @Schema(description = "Success message", example = "Order delivered successfully.")
        String message
) {
}
