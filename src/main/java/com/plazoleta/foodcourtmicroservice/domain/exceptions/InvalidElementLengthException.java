package com.plazoleta.foodcourtmicroservice.domain.exceptions;

public class InvalidElementLengthException extends IllegalArgumentException{

    public InvalidElementLengthException(String s) {
        super(s);
    }
}
