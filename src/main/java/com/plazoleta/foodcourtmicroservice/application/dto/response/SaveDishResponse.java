package com.plazoleta.foodcourtmicroservice.application.dto.response;

import java.time.LocalDateTime;

public record SaveDishResponse(
        String message,
        LocalDateTime timestamp) {
}
