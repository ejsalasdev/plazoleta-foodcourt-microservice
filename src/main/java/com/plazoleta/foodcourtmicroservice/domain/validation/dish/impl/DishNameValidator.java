package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;
import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;

public class DishNameValidator extends AbstractValidator<DishModel> {
    @Override
    protected void validateCurrent(DishModel dish, OperationType operationType) {
        if (operationType == OperationType.CREATE && dish.getName() != null && dish.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Dish name cannot be empty");
        }
    }
}
