package com.plazoleta.foodcourtmicroservice.domain.validation.dish;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.Validator;
import com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl.DishNameValidator;
import com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl.DishPriceValidator;
import com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl.DishRequiredFieldsValidator;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;

public class DishValidatorChain {
    private final Validator<DishModel> chain;

    public DishValidatorChain() {
        Validator<DishModel> required = new DishRequiredFieldsValidator();
        Validator<DishModel> name = new DishNameValidator();
        Validator<DishModel> price = new DishPriceValidator();

        required.setNext(name);
        name.setNext(price);

        this.chain = required;
    }

    public void validate(DishModel model, OperationType operationType) {
        chain.validate(model, operationType);
    }
}
