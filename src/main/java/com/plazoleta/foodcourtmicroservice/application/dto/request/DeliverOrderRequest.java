package com.plazoleta.foodcourtmicroservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request for delivering an order")
public record DeliverOrderRequest(
        @Schema(description = "Security PIN provided by the customer", example = "1234", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Security PIN cannot be blank")
        @Pattern(regexp = "^\\d{4}$", message = "Security PIN must be exactly 4 digits")
        String securityPin
) {
}
