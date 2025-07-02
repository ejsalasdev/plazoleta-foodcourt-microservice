package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class DishPriceValidatorTest {
    private final DishPriceValidator validator = new DishPriceValidator();

    @Test
    void when_validPrice_then_noException() {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", "url", 2L, 10L, true);
        assertDoesNotThrow(() -> validator.validate(model));
    }

    @Test
    void when_priceIsZeroOrNegative_then_throwInvalidElementFormatException() {
        DishModel zeroPrice = new DishModel(1L, "Pizza", new BigDecimal("0"), "desc", "url", 2L, 10L, true);
        DishModel negativePrice = new DishModel(1L, "Pizza", new BigDecimal("-1"), "desc", "url", 2L, 10L, true);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(zeroPrice));
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(negativePrice));
    }

    @Test
    void when_priceIsNotInteger_then_throwInvalidElementFormatException() {
        DishModel decimalPrice = new DishModel(1L, "Pizza", new BigDecimal("10000.50"), "desc", "url", 2L, 10L, true);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(decimalPrice));
    }
}
