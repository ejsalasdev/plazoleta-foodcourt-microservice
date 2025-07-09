package com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.AbstractPaginationValidator;

public class PageNumberValidator extends AbstractPaginationValidator {
    
    @Override
    public void validate(Integer page, Integer size, String sortBy, boolean orderAsc) {
        if (page == null || page < 0) {
            throw new InvalidElementFormatException("Page number must be 0 or greater.");
        }
        validateNext(page, size, sortBy, orderAsc);
    }
}
