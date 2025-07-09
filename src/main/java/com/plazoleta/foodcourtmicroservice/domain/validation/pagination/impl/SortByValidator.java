package com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl;

import java.util.Set;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.AbstractPaginationValidator;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;

public class SortByValidator extends AbstractPaginationValidator {

    private final Set<String> allowedFields;

    public SortByValidator(Set<String> allowedFields) {
        this.allowedFields = allowedFields;
    }

    @Override
    public void validate(Integer page, Integer size, String sortBy, boolean orderAsc) {
        if (sortBy == null || !allowedFields.contains(sortBy)) {
            throw new InvalidElementFormatException(
                    String.format(DomainMessagesConstants.PAGINATION_SORTBY_INVALID, sortBy));
        }
        validateNext(page, size, sortBy, orderAsc);
    }
}
