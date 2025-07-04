package com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

class RestaurantNameValidatorTest {
    private final RestaurantNameValidator validator = new RestaurantNameValidator();

    @Test
    void when_validName_then_noException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "+573001234567", "logo.png", 10L);
        assertDoesNotThrow(() -> validator.validate(model, OperationType.CREATE));
    }

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.NullAndEmptySource
    @org.junit.jupiter.params.provider.ValueSource(strings = {"   ", "123456"})
    void when_invalidName_then_throwInvalidElementFormatException(String invalidName) {
        RestaurantModel model = new RestaurantModel(1L, invalidName, "123456789", "Street 1", "+573001234567", "logo.png", 10L);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(model, OperationType.CREATE));
    }
}
