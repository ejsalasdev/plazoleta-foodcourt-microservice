package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

class DishImageUrlValidatorTest {

    private final DishImageUrlValidator validator = new DishImageUrlValidator();

    private RestaurantModel buildRestaurant(Long id) {
        return new RestaurantModel(id, "Restaurante Prueba", "NIT123", "Calle 1", "123456789",
                "https://logo.com/logo.jpg", 1L);
    }

    @ValueSource(strings = { "https://img.com/pizza.jpg" })
    @NullAndEmptySource
    void when_validOrEmptyOrNullImageUrl_then_noException(String imageUrl) {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", imageUrl, 2L,
                buildRestaurant(10L), true);
        assertDoesNotThrow(() -> validator.validate(model));
    }

    @Test
    void when_invalidImageUrl_then_throwInvalidElementFormatException() {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", "notaurl", 2L,
                buildRestaurant(10L), true);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(model));
    }
}
