package com.plazoleta.foodcourtmicroservice.domain.validation.restaurant;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.Validator;
import com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.impl.RestaurantNameValidator;
import com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.impl.RestaurantNitValidator;
import com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.impl.RestaurantPhoneValidator;
import com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.impl.RestaurantRequiredFieldsValidator;

public class RestaurantValidatorChain {
    private final Validator<RestaurantModel> chain;

    public RestaurantValidatorChain() {
        Validator<RestaurantModel> required = new RestaurantRequiredFieldsValidator();
        Validator<RestaurantModel> name = new RestaurantNameValidator();
        Validator<RestaurantModel> nit = new RestaurantNitValidator();
        Validator<RestaurantModel> phone = new RestaurantPhoneValidator();

        required.setNext(name);
        name.setNext(nit);
        nit.setNext(phone);

        this.chain = required;
    }

    public void validate(RestaurantModel model) {
        chain.validate(model);
    }
}
