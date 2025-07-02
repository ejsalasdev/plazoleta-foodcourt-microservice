package com.plazoleta.foodcourtmicroservice.domain.exceptions;

public class ElementAlreadyExistsException extends RuntimeException{

    public ElementAlreadyExistsException(String s) {
        super(s);
    }
}
