package com.plazoleta.foodcourtmicroservice.domain.validation.pagination.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;

class SortByValidatorTest {
    private final Set<String> allowedFields = Set.of("name", "id");
    private final SortByValidator validator = new SortByValidator(allowedFields);

    @Test
    void when_sortByIsNull_then_throwInvalidElementFormatException() {
        InvalidElementFormatException ex = assertThrows(InvalidElementFormatException.class,
                () -> validator.validate(0, 10, null, true));
        assertEquals(String.format(DomainMessagesConstants.PAGINATION_SORTBY_INVALID, (String) null), ex.getMessage());
    }

    @Test
    void when_sortByIsNotAllowed_then_throwInvalidElementFormatException() {
        String invalidField = "invalid";
        InvalidElementFormatException ex = assertThrows(InvalidElementFormatException.class,
                () -> validator.validate(0, 10, invalidField, true));
        assertEquals(String.format(DomainMessagesConstants.PAGINATION_SORTBY_INVALID, invalidField), ex.getMessage());
    }

    @Test
    void when_sortByIsAllowed_then_noException() {
        assertDoesNotThrow(() -> validator.validate(0, 10, "name", true));
        assertDoesNotThrow(() -> validator.validate(0, 10, "id", false));
    }
}
