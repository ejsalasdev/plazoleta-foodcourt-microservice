package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;
import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;

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
            throw new IllegalArgumentException(DomainMessagesConstants.DISH_NAME_NOT_EMPTY);
        }
        if (dish.getDescription() == null || dish.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException(DomainMessagesConstants.DISH_DESCRIPTION_REQUIRED);
        }
        if (dish.getRestaurant() == null || dish.getRestaurant().getId() == null) {
            throw new IllegalArgumentException(DomainMessagesConstants.DISH_RESTAURANT_ID_REQUIRED);
        }
        if (dish.getCategoryId() == null) {
            throw new IllegalArgumentException(DomainMessagesConstants.DISH_CATEGORY_ID_REQUIRED);
        }
        if (dish.getPrice() == null) {
            throw new IllegalArgumentException(DomainMessagesConstants.DISH_PRICE_REQUIRED);
        }
    }

    private void validateUpdateFields(DishModel dish) {
        if (dish.getPrice() == null) {
            throw new IllegalArgumentException(DomainMessagesConstants.DISH_PRICE_REQUIRED);
        }
        if (dish.getDescription() == null || dish.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException(DomainMessagesConstants.DISH_DESCRIPTION_REQUIRED);
        }
    }
}
