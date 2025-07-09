package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class DishNameValidatorTest {
    private final DishNameValidator validator = new DishNameValidator();

    private RestaurantModel buildRestaurant(Long id) {
        return new RestaurantModel(id, "Restaurante Prueba", "NIT123", "Calle 1", "123456789",
                "https://logo.com/logo.jpg", 1L);
    }

    void when_validName_then_noException() {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", "url", 2L, buildRestaurant(10L),
                true);
        assertDoesNotThrow(() -> validator.validate(model, OperationType.CREATE));
    }

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.ValueSource(strings = { "   " })
    void when_invalidName_then_throwInvalidElementFormatException(String invalidName) {
        DishModel model = new DishModel(1L, invalidName, new BigDecimal("10000"), "desc", "url", 2L,
                buildRestaurant(10L), true);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_nullName_then_noException() {
        DishModel model = new DishModel(1L, null, new BigDecimal("10000"), "desc", "url", 2L, buildRestaurant(10L),
                true);
        assertDoesNotThrow(() -> validator.validate(model, OperationType.CREATE));
    }
}
