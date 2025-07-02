package com.plazoleta.foodcourtmicroservice.domain.validation.restaurant;


import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;

public class RestaurantNitValidator extends AbstractValidator<RestaurantModel> {
    @Override
    protected void validateCurrent(RestaurantModel model) {
        String nit = model.getNit();
        if (nit == null || nit.trim().isEmpty()) {
            throw new InvalidElementFormatException(DomainMessagesConstants.NIT_REQUIRED);
        }
        if (!nit.matches(DomainMessagesConstants.NIT_NUMERIC_REGEX)) {
            throw new InvalidElementFormatException(DomainMessagesConstants.NIT_NUMERIC);
        }
    }
}
