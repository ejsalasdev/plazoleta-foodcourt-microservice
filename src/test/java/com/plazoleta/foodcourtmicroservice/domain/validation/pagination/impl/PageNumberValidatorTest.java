package com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageNumberValidatorTest {
    private final PageNumberValidator validator = new PageNumberValidator();

    @Test
    void when_pageIsNull_then_throwInvalidElementFormatException() {
        InvalidElementFormatException ex = assertThrows(InvalidElementFormatException.class, () ->
                validator.validate(null, 10, "name", true));
        assertEquals(DomainMessagesConstants.PAGINATION_PAGE_NUMBER_INVALID, ex.getMessage());
    }

    @Test
    void when_pageIsNegative_then_throwInvalidElementFormatException() {
        InvalidElementFormatException ex = assertThrows(InvalidElementFormatException.class, () ->
                validator.validate(-1, 10, "name", true));
        assertEquals(DomainMessagesConstants.PAGINATION_PAGE_NUMBER_INVALID, ex.getMessage());
    }

    @Test
    void when_pageIsZeroOrPositive_then_noException() {
        assertDoesNotThrow(() -> validator.validate(0, 10, "name", true));
        assertDoesNotThrow(() -> validator.validate(5, 10, "name", true));
    }
}
