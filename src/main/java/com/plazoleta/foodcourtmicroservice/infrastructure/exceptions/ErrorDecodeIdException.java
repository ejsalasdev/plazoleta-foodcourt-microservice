package com.plazoleta.foodcourtmicroservice.infrastructure.exceptions;

public class ErrorDecodeIdException extends IllegalArgumentException {

    public ErrorDecodeIdException(String message) {
        super(message);
    }

}
