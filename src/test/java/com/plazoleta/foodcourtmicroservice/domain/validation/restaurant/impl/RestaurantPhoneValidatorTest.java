package com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementLengthException;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

class RestaurantPhoneValidatorTest {
    private final RestaurantPhoneValidator validator = new RestaurantPhoneValidator();

    @Test
    void when_validPhone_then_noException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "+573001234567", "logo.png", 10L);
        assertDoesNotThrow(() -> validator.validate(model));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void when_phoneIsNullOrEmpty_then_throwInvalidElementFormatException(String phone) {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", phone, "logo.png", 10L);
        assertThrows(InvalidElementFormatException.class, () -> validator.validate(model));
    }

    @Test
    void when_phoneIsTooLong_then_throwInvalidElementLengthException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "+573001234567890", "logo.png", 10L);
        assertThrows(InvalidElementLengthException.class, () -> validator.validate(model));
    }

    @ParameterizedTest
    @ValueSource(strings = {"+57ABC123", "123-456", "phone123"})
    void when_phoneIsNotNumeric_then_throwInvalidElementFormatException(String phone) {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", phone, "logo.png", 10L);
        InvalidElementFormatException exception = assertThrows(
                InvalidElementFormatException.class,
                () -> validator.validate(model),
                "Expected InvalidElementFormatException for non-numeric phone: " + phone
        );
        assertTrue(exception.getMessage() == null || exception.getMessage().toLowerCase().contains("numeric"),
                "Exception message should mention numeric requirement");
    }
}
