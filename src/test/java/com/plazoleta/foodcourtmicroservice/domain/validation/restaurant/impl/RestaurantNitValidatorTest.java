package com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

class RestaurantNitValidatorTest {
    private final RestaurantNitValidator validator = new RestaurantNitValidator();

    @Test
    void when_validNit_then_noException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "+573001234567", "logo.png", 10L);
        assertDoesNotThrow(() -> validator.validate(model));
    }

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.NullAndEmptySource
    @org.junit.jupiter.params.provider.ValueSource(strings = {"   ", "ABC123"})
    void when_invalidNit_then_throwInvalidElementFormatException(String nit) {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", nit, "Street 1", "+573001234567", "logo.png", 10L);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(model));
    }
}
