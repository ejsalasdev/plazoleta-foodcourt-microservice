package com.plazoleta.foodcourtmicroservice.domain.validation.dish;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.RequiredFieldsException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DishValidatorChainTest {
    private final DishValidatorChain validatorChain = new DishValidatorChain();

    @Test
    void when_allFieldsValid_then_noException() {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", "https://img.com/pizza.jpg", 2L, 10L, true);
        assertDoesNotThrow(() -> validatorChain.validate(model));
    }

    @Test
    void when_missingRequiredField_then_throwRequiredFieldsException() {
        DishModel model = new DishModel(1L, null, new BigDecimal("10000"), "desc", "url", 2L, 10L, true);
        assertThrows(RequiredFieldsException.class, () -> validatorChain.validate(model));
    }

    @Test
    void when_invalidName_then_throwInvalidElementFormatException() {
        DishModel model = new DishModel(1L, "   ", new BigDecimal("10000"), "desc", "url", 2L, 10L, true);
        assertThrows(InvalidElementFormatException.class, () -> validatorChain.validate(model));
    }

    @Test
    void when_invalidPrice_then_throwInvalidElementFormatException() {
        DishModel model = new DishModel(1L, "Pizza", new BigDecimal("0"), "desc", "url", 2L, 10L, true);
        assertThrows(InvalidElementFormatException.class, () -> validatorChain.validate(model));
    }
}
