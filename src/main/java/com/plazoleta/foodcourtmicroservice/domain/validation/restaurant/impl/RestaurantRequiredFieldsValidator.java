package com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.impl;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.RequiredFieldsException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;
import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;

public class RestaurantRequiredFieldsValidator extends AbstractValidator<RestaurantModel> {
    
    @Override
    protected void validateCurrent(RestaurantModel model, OperationType operationType) {
        if (operationType == OperationType.CREATE && (model.getName() == null || model.getName().trim().isEmpty() ||
                    model.getNit() == null || model.getNit().trim().isEmpty() ||
                    model.getAddress() == null || model.getAddress().trim().isEmpty() ||
                    model.getPhoneNumber() == null || model.getPhoneNumber().trim().isEmpty() ||
                    model.getUrlLogo() == null || model.getUrlLogo().trim().isEmpty()) ||
                    model.getOwnerId() == null || model.getOwnerId() <= 0) {
                throw new RequiredFieldsException(DomainMessagesConstants.REQUIRED_FIELDS);
            }
        
    }
}
