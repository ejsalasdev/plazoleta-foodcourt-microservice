package com.plazoleta.foodcourtmicroservice.domain.validation;


import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;

public class RestaurantNitValidator extends AbstractValidator<RestaurantModel> {
    @Override
    protected void validateCurrent(RestaurantModel model) {
        String nit = model.getNit();
        if (nit == null || nit.trim().isEmpty()) {
            throw new InvalidElementFormatException("El NIT es obligatorio.");
        }
        if (!nit.matches("\\d+")) {
            throw new InvalidElementFormatException("El NIT debe ser numérico.");
        }
    }
}
