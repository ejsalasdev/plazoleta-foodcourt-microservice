package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.RequiredFieldsException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;

public class DishRequiredFieldsValidator extends AbstractValidator<DishModel> {
    @Override
    protected void validateCurrent(DishModel dish) {
        if (dish.getName() == null) {
            throw new RequiredFieldsException(DomainMessagesConstants.DISH_NAME_REQUIRED);
        }
        if (dish.getDescription() == null || dish.getDescription().trim().isEmpty()) {
            throw new RequiredFieldsException(DomainMessagesConstants.DISH_DESCRIPTION_REQUIRED);
        }
        if (dish.getRestaurantId() == null) {
            throw new RequiredFieldsException(DomainMessagesConstants.DISH_RESTAURANT_ID_REQUIRED);
        }
        if (dish.getCategoryId() == null) {
            throw new RequiredFieldsException(DomainMessagesConstants.DISH_CATEGORY_ID_REQUIRED);
        }
        if (dish.getPrice() == null) {
            throw new RequiredFieldsException(DomainMessagesConstants.DISH_PRICE_REQUIRED);
        }
    }
}
