package com.plazoleta.foodcourtmicroservice.domain.validation;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.RestaurantValidatorChain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantValidatorChainTest {

    @Test
    void when_validModel_then_noException() {
        RestaurantModel model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "+573001234567", "logo.png", 10L);
        RestaurantValidatorChain chain = new RestaurantValidatorChain();
        assertDoesNotThrow(() -> chain.validate(model));
    }

    @Test
    void when_invalidModel_then_exceptionThrown() {
        RestaurantModel model = new RestaurantModel(null, null, null, null, null, null, null);
        RestaurantValidatorChain chain = new RestaurantValidatorChain();
        assertThrows(Exception.class, () -> chain.validate(model));
    }
}
