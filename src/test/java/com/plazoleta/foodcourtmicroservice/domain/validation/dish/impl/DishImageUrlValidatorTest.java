package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class DishImageUrlValidatorTest {

    private final DishImageUrlValidator validator = new DishImageUrlValidator();

    @ParameterizedTest
    @ValueSource(strings = {"https://img.com/pizza.jpg"})
    @NullAndEmptySource
    void when_validOrEmptyOrNullImageUrl_then_noException(String imageUrl) {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", imageUrl, 2L, 10L, true);
        assertDoesNotThrow(() -> validator.validate(model));
    }

    @Test
    void when_invalidImageUrl_then_throwInvalidElementFormatException() {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", "notaurl", 2L, 10L, true);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(model));
    }
}
