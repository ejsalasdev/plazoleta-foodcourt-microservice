package com.plazoleta.foodcourtmicroservice.application.dto.request;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateDishRequest", description = "Request body for updating a dish.")
public record UpdateDishRequest(
    @Schema(description = "Restaurant ID", example = "10")
    Long restaurantId,

    @Schema(description = "Dish price (integer, greater than 0)", example = "16000")
    BigDecimal price,

    @Schema(description = "Dish description", example = "Updated description for the dish.")
    String description
) {}
