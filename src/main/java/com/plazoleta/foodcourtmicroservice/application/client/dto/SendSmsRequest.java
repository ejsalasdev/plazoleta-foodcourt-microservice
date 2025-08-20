package com.plazoleta.foodcourtmicroservice.application.client.dto;

public record SendSmsRequest(
    String phoneNumber,
    String message,
    String securityPin,
    Long orderId
) {
}
