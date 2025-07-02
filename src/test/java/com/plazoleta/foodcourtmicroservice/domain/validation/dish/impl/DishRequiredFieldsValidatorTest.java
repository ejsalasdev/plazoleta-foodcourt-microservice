package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.RequiredFieldsException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;

class DishRequiredFieldsValidatorTest {
    private final DishRequiredFieldsValidator validator = new DishRequiredFieldsValidator();

    @Test
    void when_allFieldsPresent_then_noException() {
        DishModel model = new DishModel(1L, "Hamburguesa", new BigDecimal("15000.00"), "Clásica hamburguesa", "https://img.com/hamburguesa.jpg", 2L, 10L, true);
        assertDoesNotThrow(() -> validator.validate(model));
    }

    @Test
    void when_missingName_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, null, new BigDecimal("15000.00"), "desc", "url", 2L, 10L, true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model));
    }

    @Test
    void when_missingDescription_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, "Hamburguesa", new BigDecimal("15000.00"), null, "url", 2L, 10L, true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model));
    }

    @Test
    void when_descriptionIsEmpty_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, "Hamburguesa", new BigDecimal("15000.00"), "", "url", 2L, 10L, true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model));
    }

    @Test
    void when_descriptionIsBlank_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, "Hamburguesa", new BigDecimal("15000.00"), "   ", "url", 2L, 10L, true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model));
    }

    @Test
    void when_missingRestaurantId_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, "Hamburguesa", new BigDecimal("15000.00"), "desc", "url", 2L, null, true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model));
    }

    @Test
    void when_missingCategoryId_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, "Hamburguesa", new BigDecimal("15000.00"), "desc", "url", null, 10L, true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model));
    }

    @Test
    void when_missingPrice_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, "Hamburguesa", null, "desc", "url", 2L, 10L, true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model));
    }
}
