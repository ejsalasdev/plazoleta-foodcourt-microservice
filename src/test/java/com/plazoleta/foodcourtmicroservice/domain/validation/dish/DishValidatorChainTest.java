package com.plazoleta.foodcourtmicroservice.domain.validation.dish;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.RequiredFieldsException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

class DishValidatorChainTest {

    private final DishValidatorChain validatorChain = new DishValidatorChain();

    private RestaurantModel buildRestaurant(Long id) {
        return new RestaurantModel(id, "Restaurante Prueba", "NIT123", "Calle 1", "123456789",
                "https://logo.com/logo.jpg", 1L);
    }

    @Test
    void when_allFieldsValid_then_noException() {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", "https://img.com/pizza.jpg", 2L,
                buildRestaurant(10L), true);
        assertDoesNotThrow(() -> validatorChain.validate(model, OperationType.CREATE));
    }

    @Test
    void when_missingRequiredField_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, "name", new BigDecimal("10000"), " ", "url", 2L, buildRestaurant(10L), true);
        assertThrows(RequiredFieldsException.class, () -> validatorChain.validate(model, OperationType.CREATE));
    }

    @Test
    void when_invalidName_then_throwInvalidElementFormatException() {
        DishModel model = new DishModel(1L, "   ", new BigDecimal("10000"), "desc", "url", 2L, buildRestaurant(10L),
                true);
        assertThrows(InvalidElementFormatException.class, () -> validatorChain.validate(model, OperationType.CREATE));
    }

    @Test
    void when_invalidPrice_then_throwInvalidElementFormatException() {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("0"), "desc", "url", 2L, buildRestaurant(10L),
                true);
        assertThrows(InvalidElementFormatException.class, () -> validatorChain.validate(model, OperationType.CREATE));
    }
}
