package com.plazoleta.foodcourtmicroservice.domain.exceptions;

public class CustomerHasActiveOrderException extends RuntimeException {
    public CustomerHasActiveOrderException(String message) {
        super(message);
    }
}
