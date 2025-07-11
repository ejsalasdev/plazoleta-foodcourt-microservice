package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class DishPriceValidatorTest {
    private final DishPriceValidator validator = new DishPriceValidator();

    private RestaurantModel buildRestaurant(Long id) {
        return new RestaurantModel(id, "Restaurante Prueba", "NIT123", "Calle 1", "123456789",
                "https://logo.com/logo.jpg", 1L);
    }

    @Test
    void when_validPrice_then_noException() {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", "url", 2L, buildRestaurant(10L),
                true);
        assertDoesNotThrow(() -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_priceIsZeroOrNegative_then_throwInvalidElementFormatException() {
        DishModel zeroPrice = new DishModel(1L, "Pizza", new BigDecimal("0"), "desc", "url", 2L, buildRestaurant(10L),
                true);
        DishModel negativePrice = new DishModel(1L, "Pizza", new BigDecimal("-1"), "desc", "url", 2L,
                buildRestaurant(10L), true);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(zeroPrice, OperationType.CREATE));
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(negativePrice, OperationType.CREATE));
    }

    @Test
    void when_priceIsNotInteger_then_throwInvalidElementFormatException() {
        DishModel decimalPrice = new DishModel(1L, "Pizza", new BigDecimal("10000.50"), "desc", "url", 2L,
                buildRestaurant(10L), true);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(decimalPrice, OperationType.CREATE));
    }
}
