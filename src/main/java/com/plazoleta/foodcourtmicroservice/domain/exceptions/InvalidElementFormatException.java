package com.plazoleta.foodcourtmicroservice.domain.exceptions;

public class InvalidElementFormatException extends IllegalArgumentException{

    public InvalidElementFormatException(String s) {
        super(s);
    }
}
