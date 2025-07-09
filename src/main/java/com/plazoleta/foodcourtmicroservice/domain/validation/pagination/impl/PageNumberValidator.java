package com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.AbstractPaginationValidator;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;

public class PageNumberValidator extends AbstractPaginationValidator {

    @Override
    public void validate(Integer page, Integer size, String sortBy, boolean orderAsc) {
        if (page == null || page < 0) {
            throw new InvalidElementFormatException(DomainMessagesConstants.PAGINATION_PAGE_NUMBER_INVALID);
        }
        validateNext(page, size, sortBy, orderAsc);
    }
}
