package com.plazoleta.foodcourtmicroservice.domain.exceptions;

public class InvalidOwnerException extends IllegalStateException {

    public InvalidOwnerException(String s) {
        super(s);
    }
}
