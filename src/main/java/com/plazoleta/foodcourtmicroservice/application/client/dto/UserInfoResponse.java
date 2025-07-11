package com.plazoleta.foodcourtmicroservice.application.client.dto;

public record UserInfoResponse (
    Long id,
    String role,
    Long restaurantId,
    String phoneNumber
) {

}
