package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;

import java.math.BigDecimal;

public class DishPriceValidator extends AbstractValidator<DishModel> {
    @Override
    protected void validateCurrent(DishModel dish) {
        if (dish.getPrice() != null) {
            
            if (dish.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidElementFormatException(DomainMessagesConstants.DISH_PRICE_GREATER_THAN_ZERO);
            }

            if (dish.getPrice().stripTrailingZeros().scale() > 0) {
                throw new InvalidElementFormatException(DomainMessagesConstants.DISH_PRICE_MUST_BE_INTEGER);
            }
        }
    }
}
