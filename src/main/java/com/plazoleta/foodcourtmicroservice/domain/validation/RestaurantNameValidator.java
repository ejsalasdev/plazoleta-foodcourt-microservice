package com.plazoleta.foodcourtmicroservice.domain.validation;


import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;

public class RestaurantNameValidator extends AbstractValidator<RestaurantModel> {
    @Override
    protected void validateCurrent(RestaurantModel model) {
        String name = model.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidElementFormatException("El nombre del restaurante es obligatorio.");
        }
        if (name.matches("\\d+")) {
            throw new InvalidElementFormatException("El nombre del restaurante no puede ser solo números.");
        }
    }
}
