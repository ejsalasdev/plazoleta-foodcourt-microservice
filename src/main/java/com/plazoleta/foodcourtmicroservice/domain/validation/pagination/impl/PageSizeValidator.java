package com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.AbstractPaginationValidator;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;

public class PageSizeValidator extends AbstractPaginationValidator {

    @Override
    public void validate(Integer page, Integer size, String sortBy, boolean orderAsc) {
        if (size == null || size <= 0) {
            throw new InvalidElementFormatException(DomainMessagesConstants.PAGINATION_PAGE_SIZE_INVALID);
        }
        validateNext(page, size, sortBy, orderAsc);
    }
}
