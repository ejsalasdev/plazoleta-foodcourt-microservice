package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;

public class DishNameValidator extends AbstractValidator<DishModel> {
    @Override
    protected void validateCurrent(DishModel dish) {
        if (dish.getName() != null && dish.getName().trim().isEmpty()) {
            throw new InvalidElementFormatException(DomainMessagesConstants.DISH_NAME_NOT_EMPTY);
        }
    }
}
