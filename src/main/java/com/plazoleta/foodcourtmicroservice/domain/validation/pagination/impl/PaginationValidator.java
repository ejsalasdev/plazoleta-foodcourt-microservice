package com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl;

public interface PaginationValidator {
    void validate(Integer page, Integer size, String sortBy, boolean orderAsc);
    void setNext(PaginationValidator next);
}
