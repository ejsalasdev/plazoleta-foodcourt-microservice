package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;


public class DishPriceValidator extends AbstractValidator<DishModel> {
    @Override
    protected void validateCurrent(DishModel dish, com.plazoleta.foodcourtmicroservice.domain.enums.OperationType operationType) {
        if (dish.getPrice() != null) {
            if (dish.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(DomainMessagesConstants.DISH_PRICE_GREATER_THAN_ZERO);
            }
            if (dish.getPrice().stripTrailingZeros().scale() > 0) {
                throw new IllegalArgumentException(DomainMessagesConstants.DISH_PRICE_MUST_BE_INTEGER);
            }
        }
    }
}
