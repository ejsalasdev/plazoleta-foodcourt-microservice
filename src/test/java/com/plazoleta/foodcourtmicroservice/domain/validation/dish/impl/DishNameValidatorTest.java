package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.model.CategoryModel;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

class DishNameValidatorTest {
    private final DishNameValidator validator = new DishNameValidator();

    private RestaurantModel buildRestaurant(Long id) {
        return new RestaurantModel(id, "Restaurante Prueba", "NIT123", "Calle 1", "123456789",
                "https://logo.com/logo.jpg", 1L);
    }

    private CategoryModel buildCategory(Long id) {
        return new CategoryModel(id, "cat", "desc");
    }

    void when_validName_then_noException() {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", "url", buildCategory(2L),
                buildRestaurant(10L),
                true);
        assertDoesNotThrow(() -> validator.validate(model, OperationType.CREATE));
    }

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.ValueSource(strings = { "   " })
    void when_invalidName_then_throwInvalidElementFormatException(String invalidName) {
        DishModel model = new DishModel(1L, invalidName, new BigDecimal("10000"), "desc", "url", buildCategory(2L),
                buildRestaurant(10L), true);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_nullName_then_noException() {
        DishModel model = new DishModel(1L, null, new BigDecimal("10000"), "desc", "url", buildCategory(2L),
                buildRestaurant(10L),
                true);
        assertDoesNotThrow(() -> validator.validate(model, OperationType.CREATE));
    }
}
