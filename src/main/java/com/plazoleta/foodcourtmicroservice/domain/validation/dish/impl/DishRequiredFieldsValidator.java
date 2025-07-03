package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;
import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;

public class DishRequiredFieldsValidator extends AbstractValidator<DishModel> {
    @Override
    protected void validateCurrent(DishModel dish, OperationType operationType) {
        if (operationType == OperationType.CREATE) {
            validateCreateFields(dish);
        } else if (operationType == OperationType.UPDATE) {
            validateUpdateFields(dish);
        }
    }

    private void validateCreateFields(DishModel dish) {
        if (dish.getName() == null || dish.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Dish name cannot be empty");
        }
        if (dish.getDescription() == null || dish.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Dish description cannot be empty");
        }
        if (dish.getRestaurant() == null || dish.getRestaurant().getId() == null) {
            throw new IllegalArgumentException("Restaurant ID is required");
        }
        if (dish.getCategoryId() == null) {
            throw new IllegalArgumentException("Category ID is required");
        }
        if (dish.getPrice() == null) {
            throw new IllegalArgumentException("Dish price is required");
        }
    }

    private void validateUpdateFields(DishModel dish) {
        if (dish.getPrice() == null) {
            throw new IllegalArgumentException("Dish price is required");
        }
        if (dish.getDescription() == null || dish.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Dish description cannot be empty");
        }
    }
}
