package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;


public class DishPriceValidator extends AbstractValidator<DishModel> {
    @Override
    protected void validateCurrent(DishModel dish, com.plazoleta.foodcourtmicroservice.domain.enums.OperationType operationType) {
        if (dish.getPrice() != null) {
            if (dish.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Dish price must be greater than 0");
            }
            if (dish.getPrice().stripTrailingZeros().scale() > 0) {
                throw new IllegalArgumentException("Dish price must be an integer");
            }
        }
    }
}
