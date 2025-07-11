package com.plazoleta.foodcourtmicroservice.application.dto.response;

import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for marking an order as ready")
public record OrderReadyResponse(
        @Schema(description = "Order ID", example = "1")
        Long id,
        
        @Schema(description = "Updated order status", example = "READY")
        OrderStatusEnum status,
        
        @Schema(description = "Success message", example = "Order marked as ready successfully and notification sent to customer.")
        String message
) {
}
