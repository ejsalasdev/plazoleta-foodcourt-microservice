package com.plazoleta.foodcourtmicroservice.application.client.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
    Long id,
    String phoneNumber,
    String message,
    Long orderId,
    String status,
    LocalDateTime createdAt,
    LocalDateTime sentAt,
    String providerResponse
) {
}
