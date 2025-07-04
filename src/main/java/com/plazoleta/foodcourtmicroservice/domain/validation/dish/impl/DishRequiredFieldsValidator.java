package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.RequiredFieldsException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;

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
        if (dish.getName() == null) {
            throw new RequiredFieldsException(DomainMessagesConstants.DISH_NAME_NOT_EMPTY);
        }
        if (dish.getDescription() == null || dish.getDescription().trim().isEmpty()) {
            throw new RequiredFieldsException(DomainMessagesConstants.DISH_DESCRIPTION_REQUIRED);
        }
        if (dish.getRestaurant() == null || dish.getRestaurant().getId() == null) {
            throw new RequiredFieldsException(DomainMessagesConstants.DISH_RESTAURANT_ID_REQUIRED);
        }
        if (dish.getCategoryId() == null) {
            throw new RequiredFieldsException(DomainMessagesConstants.DISH_CATEGORY_ID_REQUIRED);
        }
        if (dish.getPrice() == null) {
            throw new RequiredFieldsException(DomainMessagesConstants.DISH_PRICE_REQUIRED);
        }
    }

    private void validateUpdateFields(DishModel dish) {
        if (dish.getPrice() == null) {
            throw new RequiredFieldsException(DomainMessagesConstants.DISH_PRICE_REQUIRED);
        }
        if (dish.getDescription() == null || dish.getDescription().trim().isEmpty()) {
            throw new RequiredFieldsException(DomainMessagesConstants.DISH_DESCRIPTION_REQUIRED);
        }
    }
}
