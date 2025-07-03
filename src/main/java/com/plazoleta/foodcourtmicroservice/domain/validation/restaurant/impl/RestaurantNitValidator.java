package com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.impl;


import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;
import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;

public class RestaurantNitValidator extends AbstractValidator<RestaurantModel> {
    @Override
    protected void validateCurrent(RestaurantModel model, OperationType operationType) {
        if (operationType == OperationType.CREATE) {
            String nit = model.getNit();
            if (nit == null || nit.trim().isEmpty()) {
                throw new InvalidElementFormatException(DomainMessagesConstants.NIT_REQUIRED);
            }
            if (!nit.matches(DomainMessagesConstants.NIT_NUMERIC_REGEX)) {
                throw new InvalidElementFormatException(DomainMessagesConstants.NIT_NUMERIC);
            }
        }
    }
}
