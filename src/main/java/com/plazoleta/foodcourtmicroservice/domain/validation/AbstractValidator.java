package com.plazoleta.foodcourtmicroservice.domain.validation;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;

public abstract class AbstractValidator<T> implements Validator<T> {
    private Validator<T> next;

    @Override
    public void setNext(Validator<T> next) {
        this.next = next;
    }

    @Override
    public void validate(T model, OperationType operationType) {
        validateCurrent(model, operationType);
        if (next != null) {
            next.validate(model, operationType);
        }
    }

    protected abstract void validateCurrent(T model, OperationType operationType);
}
