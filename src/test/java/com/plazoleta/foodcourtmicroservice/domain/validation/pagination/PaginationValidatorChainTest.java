package com.plazoleta.foodcourtmicroservice.domain.validation.pagination;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;

class PaginationValidatorChainTest {
    private PaginationValidatorChain validatorChain;

    @BeforeEach
    void setUp() {
        validatorChain = new PaginationValidatorChain(Set.of("name", "id"));
    }

    @Test
    void when_allParametersValid_then_noException() {
        assertDoesNotThrow(() -> validatorChain.validate(0, 10, "name", true));
        assertDoesNotThrow(() -> validatorChain.validate(1, 5, "id", false));
    }

    @Test
    void when_invalidPage_then_throwInvalidElementFormatException() {
        InvalidElementFormatException ex = assertThrows(InvalidElementFormatException.class,
                () -> validatorChain.validate(-1, 10, "name", true));
        assertEquals(DomainMessagesConstants.PAGINATION_PAGE_NUMBER_INVALID, ex.getMessage());
    }

    @Test
    void when_invalidSize_then_throwInvalidElementFormatException() {
        InvalidElementFormatException ex = assertThrows(InvalidElementFormatException.class,
                () -> validatorChain.validate(0, 0, "name", true));
        assertEquals(DomainMessagesConstants.PAGINATION_PAGE_SIZE_INVALID, ex.getMessage());
    }

    @Test
    void when_invalidSortBy_then_throwInvalidElementFormatException() {
        String invalidField = "invalid";
        InvalidElementFormatException ex = assertThrows(InvalidElementFormatException.class,
                () -> validatorChain.validate(0, 10, invalidField, true));
        assertEquals(String.format(DomainMessagesConstants.PAGINATION_SORTBY_INVALID, invalidField), ex.getMessage());
    }
}
