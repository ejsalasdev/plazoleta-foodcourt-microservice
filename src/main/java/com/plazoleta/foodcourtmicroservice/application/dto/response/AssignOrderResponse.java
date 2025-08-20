package com.plazoleta.foodcourtmicroservice.application.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record AssignOrderResponse(
        @Schema(description = "Success message", example = "Order assigned successfully and status changed to IN_PREPARATION")
        String message,

        @Schema(description = "Response timestamp", example = "2025-07-10T22:30:00")
        LocalDateTime timestamp
) {
}
