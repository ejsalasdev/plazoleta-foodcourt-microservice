package com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;

class PageSizeValidatorTest {
    private final PageSizeValidator validator = new PageSizeValidator();

    @Test
    void when_sizeIsNull_then_throwInvalidElementFormatException() {
        InvalidElementFormatException ex = assertThrows(InvalidElementFormatException.class,
                () -> validator.validate(0, null, "name", true));
        assertEquals(DomainMessagesConstants.PAGINATION_PAGE_SIZE_INVALID, ex.getMessage());
    }

    @Test
    void when_sizeIsZeroOrNegative_then_throwInvalidElementFormatException() {
        InvalidElementFormatException ex1 = assertThrows(InvalidElementFormatException.class,
                () -> validator.validate(0, 0, "name", true));
        assertEquals(DomainMessagesConstants.PAGINATION_PAGE_SIZE_INVALID, ex1.getMessage());
        InvalidElementFormatException ex2 = assertThrows(InvalidElementFormatException.class,
                () -> validator.validate(0, -5, "name", true));
        assertEquals(DomainMessagesConstants.PAGINATION_PAGE_SIZE_INVALID, ex2.getMessage());
    }

    @Test
    void when_sizeIsPositive_then_noException() {
        assertDoesNotThrow(() -> validator.validate(0, 1, "name", true));
        assertDoesNotThrow(() -> validator.validate(0, 10, "name", true));
    }
}
