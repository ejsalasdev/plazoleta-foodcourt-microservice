package com.plazoleta.foodcourtmicroservice.domain.validation;

public abstract class AbstractValidator<T> implements Validator<T> {
    private Validator<T> next;

    @Override
    public void setNext(Validator<T> next) {
        this.next = next;
    }

    @Override
    public void validate(T model) {
        validateCurrent(model);
        if (next != null) {
            next.validate(model);
        }
    }

    protected abstract void validateCurrent(T model);
}
