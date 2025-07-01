package com.plazoleta.foodcourtmicroservice.application.dto.request;

public record SaveRestaurantRequest(
        String name,
        String nit,
        String address,
        String phoneNumber,
        String urlLogo) {

}
