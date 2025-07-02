package com.plazoleta.foodcourtmicroservice.application.dto.request;

public record SaveDishRequest(
    String name,
    Integer price,
    String description,
    String urlImage,
    Long categoryId,
    Long restaurantId
) {}
