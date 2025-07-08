package com.plazoleta.foodcourtmicroservice.domain.exceptions;

public class UnauthorizedOperationException extends IllegalStateException {
    public UnauthorizedOperationException(String s) {
        super(s);
    }
}
