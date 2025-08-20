package com.plazoleta.foodcourtmicroservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request for delivering an order")
public record DeliverOrderRequest(
                @Schema(description = "Security PIN provided by the customer", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED) String securityPin) {
}
