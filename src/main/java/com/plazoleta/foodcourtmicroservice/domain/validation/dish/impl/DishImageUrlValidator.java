package com.plazoleta.foodcourtmicroservice.domain.validation.dish.impl;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.validation.AbstractValidator;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;

import java.util.regex.Pattern;

public class DishImageUrlValidator extends AbstractValidator<DishModel> {

    private static final Pattern IMAGE_URL_PATTERN = Pattern.compile(
            DomainMessagesConstants.URL_IMAGE_REGEX,
            Pattern.CASE_INSENSITIVE);

    @Override
    protected void validateCurrent(DishModel dishModel) {
        String imageUrl = dishModel.getUrlImage();
        if (imageUrl != null && !imageUrl.trim().isEmpty() && !IMAGE_URL_PATTERN.matcher(imageUrl).matches()) {
            throw new InvalidElementFormatException(DomainMessagesConstants.DISH_IMAGE_URL_INVALID);
        }

    }
}
