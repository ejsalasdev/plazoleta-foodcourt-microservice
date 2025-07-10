package com.plazoleta.foodcourtmicroservice.domain.validation.pagination;

import java.util.Set;

import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl.PageNumberValidator;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl.PageSizeValidator;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl.PaginationValidator;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl.SortByValidator;

public class PaginationValidatorChain {
    
    private final PaginationValidator chain;

    public PaginationValidatorChain(Set<String> allowedSortFields) {
        PageNumberValidator pageNumberValidator = new PageNumberValidator();
        PageSizeValidator pageSizeValidator = new PageSizeValidator();
        SortByValidator sortByValidator = new SortByValidator(allowedSortFields);
        pageNumberValidator.setNext(pageSizeValidator);
        pageSizeValidator.setNext(sortByValidator);
        this.chain = pageNumberValidator;
    }

    public void validate(Integer page, Integer size, String sortBy, boolean orderAsc) {
        chain.validate(page, size, sortBy, orderAsc);
    }

    public void validate(Integer page, Integer size) {
        // Use default values for sortBy and orderAsc when not needed
        chain.validate(page, size, "id", true);
    }
}
