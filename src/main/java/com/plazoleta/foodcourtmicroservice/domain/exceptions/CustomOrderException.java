package com.plazoleta.foodcourtmicroservice.domain.exceptions;

public class CustomOrderException extends IllegalArgumentException {

    public CustomOrderException(String message) {
        super(message);
    }
}
