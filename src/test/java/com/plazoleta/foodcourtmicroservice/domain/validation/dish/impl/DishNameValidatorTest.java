package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class DishNameValidatorTest {
    private final DishNameValidator validator = new DishNameValidator();

    @Test
    void when_validName_then_noException() {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", "url", 2L, 10L, true);
        assertDoesNotThrow(() -> validator.validate(model));
    }

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.ValueSource(strings = {"   "})
    void when_invalidName_then_throwInvalidElementFormatException(String invalidName) {
        DishModel model = new DishModel(1L, invalidName, new BigDecimal("10000"), "desc", "url", 2L, 10L, true);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(model));
    }

    @Test
    void when_nullName_then_noException() {
        DishModel model = new DishModel(1L, null, new BigDecimal("10000"), "desc", "url", 2L, 10L, true);
        assertDoesNotThrow(() -> validator.validate(model));
    }
}
