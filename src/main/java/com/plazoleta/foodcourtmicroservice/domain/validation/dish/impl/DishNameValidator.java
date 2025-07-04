package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;

public class DishNameValidator extends AbstractValidator<DishModel> {
    @Override
    protected void validateCurrent(DishModel dish, OperationType operationType) {
        if (operationType == OperationType.CREATE && dish.getName() != null && dish.getName().trim().isEmpty()) {
            throw new InvalidElementFormatException(DomainMessagesConstants.DISH_NAME_NOT_EMPTY);
        }
    }
}
