package com.plazoleta.foodcourtmicroservice.domain.validation;


import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementLengthException;

public class RestaurantPhoneValidator extends AbstractValidator<RestaurantModel> {
    @Override
    protected void validateCurrent(RestaurantModel model) {
        String phone = model.getPhoneNumber();
        if (phone == null || phone.trim().isEmpty()) {
            throw new InvalidElementFormatException("El teléfono es obligatorio.");
        }
        if (phone.length() > 13) {
            throw new InvalidElementLengthException("El teléfono no puede tener más de 13 caracteres.");
        }
        if (!phone.matches("^\\+?\\d+$")) {
            throw new InvalidElementFormatException("El teléfono debe ser numérico y puede iniciar con '+'.");
        }
    }
}
