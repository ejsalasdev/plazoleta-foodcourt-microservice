package com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.impl;


import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;
import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;

public class RestaurantNameValidator extends AbstractValidator<RestaurantModel> {
    @Override
    protected void validateCurrent(RestaurantModel model, OperationType operationType) {
        if (operationType == OperationType.CREATE) {
            String name = model.getName();
            if (name == null || name.trim().isEmpty()) {
                throw new InvalidElementFormatException(DomainMessagesConstants.RESTAURANT_NAME_REQUIRED);
            }
            if (name.matches(DomainMessagesConstants.NAME_ONLY_NUMBERS_REGEX)) {
                throw new InvalidElementFormatException(DomainMessagesConstants.RESTAURANT_NAME_NUMERIC);
            }
        }
    }
}
