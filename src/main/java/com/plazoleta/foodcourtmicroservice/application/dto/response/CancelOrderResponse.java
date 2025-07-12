package com.plazoleta.foodcourtmicroservice.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response returned when an order is successfully cancelled")
public record CancelOrderResponse(
    @Schema(description = "Unique identifier of the cancelled order", example = "1")
    Long orderId,
    
    @Schema(description = "Confirmation message about the order cancellation", 
            example = "Order has been cancelled successfully and notification sent to customer.")
    String message
) {
}
