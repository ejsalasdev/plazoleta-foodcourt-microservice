package com.plazoleta.foodcourtmicroservice.domain.validation;

public interface Validator<T> {
    void validate(T model);
    void setNext(Validator<T> next);
}
