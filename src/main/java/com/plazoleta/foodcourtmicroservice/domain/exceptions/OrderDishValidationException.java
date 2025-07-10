package com.plazoleta.foodcourtmicroservice.domain.exceptions;

public class OrderDishValidationException extends RuntimeException {
    public OrderDishValidationException(String message) {
        super(message);
    }
}
