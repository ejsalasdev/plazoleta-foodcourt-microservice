package com.plazoleta.foodcourtmicroservice.domain.validation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;

class AbstractValidatorTest {

    static class DummyValidator extends AbstractValidator<String> {
        boolean validated = false;
        @Override
        protected void validateCurrent(String model, OperationType operationType) {
            validated = true;
        }
    }

    @Test
    void when_validate_then_validateCurrentIsCalled() {
        DummyValidator validator = new DummyValidator();
        validator.validate("test", OperationType.CREATE);
        assertTrue(validator.validated, "validateCurrent should be called");
    }

    @Test
    void when_chain_then_nextValidatorIsCalled() {
        DummyValidator first = new DummyValidator();
        DummyValidator second = new DummyValidator();
        first.setNext(second);

        first.validate("test", OperationType.CREATE);

        assertTrue(first.validated, "First validator should be called");
        assertTrue(second.validated, "Second validator should be called");
    }
}
