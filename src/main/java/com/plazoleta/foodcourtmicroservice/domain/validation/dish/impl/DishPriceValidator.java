package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;

import java.math.BigDecimal;

public class DishPriceValidator extends AbstractValidator<DishModel> {
    @Override
    protected void validateCurrent(DishModel dish) {
        if (dish.getPrice() == null || dish.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Dish price must be greater than 0");
        }
    }
}
