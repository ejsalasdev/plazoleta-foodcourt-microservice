package com.plazoleta.foodcourtmicroservice.domain.validation;


import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementLengthException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;

public class RestaurantPhoneValidator extends AbstractValidator<RestaurantModel> {
    @Override
    protected void validateCurrent(RestaurantModel model) {
        String phone = model.getPhoneNumber();
        if (phone == null || phone.trim().isEmpty()) {
            throw new InvalidElementFormatException(DomainMessagesConstants.PHONE_REQUIRED);
        }
        if (phone.length() > DomainMessagesConstants.PHONE_MAX_LENGTH) {
            throw new InvalidElementLengthException(String.format(DomainMessagesConstants.PHONE_LENGTH, DomainMessagesConstants.PHONE_MAX_LENGTH));
        }
        if (!phone.matches(DomainMessagesConstants.PHONE_REGEX)) {
            throw new InvalidElementFormatException(DomainMessagesConstants.PHONE_FORMAT);
        }
    }
}
