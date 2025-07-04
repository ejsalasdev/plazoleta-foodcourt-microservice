package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.RequiredFieldsException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;

class DishRequiredFieldsValidatorTest {
    private final DishRequiredFieldsValidator validator = new DishRequiredFieldsValidator();


    private RestaurantModel buildRestaurant(Long id) {
        return new RestaurantModel(id, "Restaurante Prueba", "NIT123", "Calle 1", "123456789", "https://logo.com/logo.jpg", 1L);
    }

    @Test
    void when_allFieldsPresent_then_noException() {
        DishModel model = new DishModel(1L, "Hamburguesa", new BigDecimal("15000.00"), "Clásica hamburguesa", "https://img.com/hamburguesa.jpg", 10L, buildRestaurant(2L), true);
        assertDoesNotThrow(() -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_missingName_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, null, new BigDecimal("15000.00"), "desc", "url", 10L, buildRestaurant(2L), true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_missingDescription_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, "Hamburguesa", new BigDecimal("15000.00"), null, "url", 10L, buildRestaurant(2L), true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_descriptionIsEmpty_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, "Hamburguesa", new BigDecimal("15000.00"), "", "url", 10L, buildRestaurant(2L), true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_descriptionIsBlank_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, "Hamburguesa", new BigDecimal("15000.00"), "   ", "url", 10L, buildRestaurant(2L), true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_missingRestaurant_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, "Hamburguesa", new BigDecimal("15000.00"), "desc", "url", 10L, null, true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_missingCategoryId_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, "Hamburguesa", new BigDecimal("15000.00"), "desc", "url", null, buildRestaurant(2L), true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }

    @Test
    void when_missingPrice_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, "Hamburguesa", null, "desc", "url", 10L, buildRestaurant(2L), true);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.CREATE));
    }

    // --- UPDATE tests ---
    @Test
    void when_update_withAllFieldsPresent_then_noException() {
        DishModel model = new DishModel();
        model.setPrice(new BigDecimal("12000.00"));
        model.setDescription("Nueva descripción");
        assertDoesNotThrow(() -> validator.validate(model, OperationType.UPDATE));
    }

    @Test
    void when_update_missingPrice_then_throwRequiredFieldsException() {
        DishModel model = new DishModel();
        model.setDescription("desc");
        model.setPrice(null);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.UPDATE));
    }

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.NullAndEmptySource
    @org.junit.jupiter.params.provider.ValueSource(strings = {"   "})
    void when_update_invalidDescription_then_throwRequiredFieldsException(String description) {
        DishModel model = new DishModel();
        model.setPrice(new BigDecimal("12000.00"));
        model.setDescription(description);
        assertThrows(RequiredFieldsException.class, () -> validator.validate(model, OperationType.UPDATE));
    }
}
