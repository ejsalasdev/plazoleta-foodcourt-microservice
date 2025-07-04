package com.plazoleta.foodcourtmicroservice.infrastructure.exceptions;

public class ErrorDecodeAuthoritiesException extends IllegalArgumentException {

    public ErrorDecodeAuthoritiesException(String message) {
        super(message);
    }

}
