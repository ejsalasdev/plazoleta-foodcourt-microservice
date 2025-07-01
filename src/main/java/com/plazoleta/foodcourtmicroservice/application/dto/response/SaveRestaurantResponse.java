package com.plazoleta.foodcourtmicroservice.application.dto.response;

import java.time.LocalDateTime;

public record SaveRestaurantResponse(
        String message,
        LocalDateTime timestamp) {

}
