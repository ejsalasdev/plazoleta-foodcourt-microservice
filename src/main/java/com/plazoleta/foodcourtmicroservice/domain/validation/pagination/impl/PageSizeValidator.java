package com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.AbstractPaginationValidator;

public class PageSizeValidator extends AbstractPaginationValidator {

    @Override
    public void validate(Integer page, Integer size, String sortBy, boolean orderAsc) {
        if (size == null || size <= 0) {
            throw new InvalidElementFormatException("Page size must be greater than 0.");
        }
        validateNext(page, size, sortBy, orderAsc);
    }
}
