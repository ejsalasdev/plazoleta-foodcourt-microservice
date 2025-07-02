package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;

public class DishNameValidator extends AbstractValidator<DishModel> {
    @Override
    protected void validateCurrent(DishModel dish) {
        if (dish.getName() == null || dish.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Dish name cannot be empty");
        }
    }
}
