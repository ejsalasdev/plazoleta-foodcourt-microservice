package com.plazoleta.foodcourtmicroservice.domain.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractValidatorTest {

    static class DummyValidator extends AbstractValidator<String> {
        boolean validated = false;
        @Override
        protected void validateCurrent(String model) {
            validated = true;
        }
    }

    @Test
    void when_validate_then_validateCurrentIsCalled() {
        DummyValidator validator = new DummyValidator();
        validator.validate("test");
        assertTrue(validator.validated, "validateCurrent should be called");
    }

    @Test
    void when_chain_then_nextValidatorIsCalled() {
        DummyValidator first = new DummyValidator();
        DummyValidator second = new DummyValidator();
        first.setNext(second);

        first.validate("test");

        assertTrue(first.validated, "First validator should be called");
        assertTrue(second.validated, "Second validator should be called");
    }
}
