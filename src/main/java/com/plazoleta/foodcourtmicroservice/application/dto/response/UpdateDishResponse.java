package com.plazoleta.foodcourtmicroservice.application.dto.response;

import java.time.LocalDateTime;

public record UpdateDishResponse(
        String message,
        LocalDateTime timestamp) {
}
