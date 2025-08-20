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

class DishPriceValidatorTest {
    private final DishPriceValidator validator = new DishPriceValidator();

    private RestaurantModel buildRestaurant(Long id) {
        return new RestaurantModel(id, "Restaurante Prueba", "NIT123", "Calle 1", "123456789",
                "https://logo.com/logo.jpg", 1L);
    }

    private CategoryModel buildCategory(Long id) {
        return new CategoryModel(id, "cat", "desc");
    }

    @Test
    void when_validPrice_then_noException() {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", "url", buildCategory(2L),
                buildRestaurant(10L),
                true);
        assertDoesNotThrow(() -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_priceIsZeroOrNegative_then_throwInvalidElementFormatException() {
        DishModel zeroPrice = new DishModel(1L, "Pizza", new BigDecimal("0"), "desc", "url", buildCategory(2L),
                buildRestaurant(10L),
                true);
        DishModel negativePrice = new DishModel(1L, "Pizza", new BigDecimal("-1"), "desc", "url", buildCategory(2L),
                buildRestaurant(10L), true);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(zeroPrice, OperationType.CREATE));
        assertThrows(InvalidElementFormatException.class,
                () -> validator.validate(negativePrice, OperationType.CREATE));
    }

    @Test
    void when_priceIsNotInteger_then_throwInvalidElementFormatException() {
        DishModel decimalPrice = new DishModel(1L, "Pizza", new BigDecimal("10000.50"), "desc", "url",
                buildCategory(2L),
                buildRestaurant(10L), true);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(decimalPrice, OperationType.CREATE));
    }
}
