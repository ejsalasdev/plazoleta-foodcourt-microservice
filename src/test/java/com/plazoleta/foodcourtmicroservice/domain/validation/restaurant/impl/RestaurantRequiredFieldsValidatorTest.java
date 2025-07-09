package com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.impl;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.RequiredFieldsException;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

class RestaurantRequiredFieldsValidatorTest {
    private final RestaurantRequiredFieldsValidator validator = new RestaurantRequiredFieldsValidator();

    @Test
    void when_allFieldsPresent_then_noException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "+573001234567", "logo.png", 10L);
        assertDoesNotThrow(() -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_missingName_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, null, "123456789", "Street 1", "+573001234567", "logo.png", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }
    @Test
    void when_nameIsEmpty_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "", "123456789", "Street 1", "+573001234567", "logo.png", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }
    @Test
    void when_nameIsBlank_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "   ", "123456789", "Street 1", "+573001234567", "logo.png", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_missingNit_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", null, "Street 1", "+573001234567", "logo.png", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }
    @Test
    void when_nitIsEmpty_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "", "Street 1", "+573001234567", "logo.png", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }
    @Test
    void when_nitIsBlank_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "   ", "Street 1", "+573001234567", "logo.png", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_missingAddress_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", null, "+573001234567", "logo.png", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }
    @Test
    void when_addressIsEmpty_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "", "+573001234567", "logo.png", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }
    @Test
    void when_addressIsBlank_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "   ", "+573001234567", "logo.png", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_missingPhone_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", null, "logo.png", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }
    @Test
    void when_phoneIsEmpty_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "", "logo.png", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }
    @Test
    void when_phoneIsBlank_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "   ", "logo.png", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_missingUrlLogo_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "+573001234567", null, 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }
    @Test
    void when_urlLogoIsEmpty_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "+573001234567", "", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }
    @Test
    void when_urlLogoIsBlank_then_throwRequiredFieldsException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "+573001234567", "   ", 10L);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }
}
