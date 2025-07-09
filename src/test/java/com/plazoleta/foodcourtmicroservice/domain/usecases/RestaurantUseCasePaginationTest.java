package com.plazoleta.foodcourtmicroservice.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.AuthenticatedUserPort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.UserServicePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;
import com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.RestaurantValidatorChain;

class RestaurantUseCasePaginationTest {

    @Mock
    private RestaurantPersistencePort persistencePort;
    @Mock
    private RestaurantValidatorChain validatorChain;
    @Mock
    private UserServicePort userServicePort;
    @Mock
    private AuthenticatedUserPort authenticatedUserPort;
    @Mock
    private com.plazoleta.foodcourtmicroservice.domain.validation.pagination.PaginationValidatorChain paginationValidatorChain;

    @InjectMocks
    private RestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new RestaurantUseCase(persistencePort, validatorChain, userServicePort, authenticatedUserPort, paginationValidatorChain);
    }

    @Test
    void when_findAll_withValidParams_then_returnPageInfo() {
        // Arrange
        RestaurantModel r1 = new RestaurantModel(1L, "A", "123", "Dir1", "+573001", "logo1.png", 10L);
        RestaurantModel r2 = new RestaurantModel(2L, "B", "456", "Dir2", "+573002", "logo2.png", 11L);
        PageInfo<RestaurantModel> pageInfo = new PageInfo<>(Arrays.asList(r1, r2), 2, 1, 0, 2, false, false);
        when(persistencePort.findAll(0, 2, "name", true)).thenReturn(pageInfo);

        // Act
        PageInfo<RestaurantModel> result = useCase.findAll(0, 2, "name", true);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("A", result.getContent().get(0).getName());
        verify(persistencePort, times(1)).findAll(0, 2, "name", true);
    }

    @Test
    void when_findAll_withInvalidPage_then_throwInvalidElementFormatException() {
        // Arrange
        doThrow(new InvalidElementFormatException(DomainMessagesConstants.PAGINATION_PAGE_NUMBER_INVALID))
                .when(paginationValidatorChain).validate(-1, 2, "name", true);
        // Act & Assert
        InvalidElementFormatException ex = assertThrows(InvalidElementFormatException.class,
                () -> useCase.findAll(-1, 2, "name", true));
        assertEquals(DomainMessagesConstants.PAGINATION_PAGE_NUMBER_INVALID, ex.getMessage());
        verify(persistencePort, never()).findAll(anyInt(), anyInt(), anyString(), anyBoolean());
    }

    @Test
    void when_findAll_withInvalidSize_then_throwInvalidElementFormatException() {
        // Arrange
        doThrow(new InvalidElementFormatException(DomainMessagesConstants.PAGINATION_PAGE_SIZE_INVALID))
                .when(paginationValidatorChain).validate(0, 0, "name", true);
        // Act & Assert
        InvalidElementFormatException ex = assertThrows(InvalidElementFormatException.class,
                () -> useCase.findAll(0, 0, "name", true));
        assertEquals(DomainMessagesConstants.PAGINATION_PAGE_SIZE_INVALID, ex.getMessage());
        verify(persistencePort, never()).findAll(anyInt(), anyInt(), anyString(), anyBoolean());
    }

    @Test
    void when_findAll_withInvalidSortBy_then_throwInvalidElementFormatException() {
        // Arrange
        doThrow(new InvalidElementFormatException(String.format(DomainMessagesConstants.PAGINATION_SORTBY_INVALID, "invalid")))
                .when(paginationValidatorChain).validate(0, 2, "invalid", true);
        // Act & Assert
        InvalidElementFormatException ex = assertThrows(InvalidElementFormatException.class,
                () -> useCase.findAll(0, 2, "invalid", true));
        assertEquals(String.format(DomainMessagesConstants.PAGINATION_SORTBY_INVALID, "invalid"), ex.getMessage());
        verify(persistencePort, never()).findAll(anyInt(), anyInt(), anyString(), anyBoolean());
    }
}
