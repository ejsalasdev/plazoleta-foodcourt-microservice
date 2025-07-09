package com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl;

import java.util.Set;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.AbstractPaginationValidator;

public class SortByValidator extends AbstractPaginationValidator {

    private final Set<String> allowedFields;

    public SortByValidator(Set<String> allowedFields) {
        this.allowedFields = allowedFields;
    }

    @Override
    public void validate(Integer page, Integer size, String sortBy, boolean orderAsc) {
        if (sortBy == null || !allowedFields.contains(sortBy)) {
            throw new InvalidElementFormatException("Invalid sortBy field: " + sortBy);
        }
        validateNext(page, size, sortBy, orderAsc);
    }
}
