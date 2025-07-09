package com.plazoleta.foodcourtmicroservice.domain.validation;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;

public interface Validator<T> {
    void validate(T model, OperationType operationType);
    void setNext(Validator<T> next);
}
