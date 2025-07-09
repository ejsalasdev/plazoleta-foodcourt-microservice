package com.plazoleta.foodcourtmicroservice.domain.validation.pagination;

import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl.PaginationValidator;

public abstract class AbstractPaginationValidator implements PaginationValidator {
    private PaginationValidator next;

    @Override
    public void setNext(PaginationValidator next) {
        this.next = next;
    }

    protected void validateNext(Integer page, Integer size, String sortBy, boolean orderAsc) {
        if (next != null) {
            next.validate(page, size, sortBy, orderAsc);
        }
    }
}
