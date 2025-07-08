package com.plazoleta.foodcourtmicroservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetDishActiveRequest {
    @NotNull
    @Schema(description = "ID of the restaurant", example = "1")
    private Long restaurantId;

    @NotNull
    @Schema(description = "Whether the dish should be active (true) or inactive (false)", example = "true")
    private Boolean active;
}
