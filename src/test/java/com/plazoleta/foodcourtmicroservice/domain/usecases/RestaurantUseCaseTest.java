package com.plazoleta.foodcourtmicroservice.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementAlreadyExistsException;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.RestaurantValidatorChain;

class RestaurantUseCaseTest {

    @Mock
    private RestaurantPersistencePort persistencePort;

    @Mock
    private RestaurantValidatorChain validatorChain;

    @InjectMocks
    private RestaurantUseCase useCase;

    private RestaurantModel model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "+573001234567", "logo.png", 10L);
    }

    @Test
    void when_save_withValidRestaurant_then_restaurantIsSaved() {
        // Arrange
        when(persistencePort.existsByNit(model.getNit())).thenReturn(false);

        // Act
        useCase.save(model);

        // Assert
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(persistencePort, times(1)).existsByNit(model.getNit());
        verify(persistencePort, times(1)).save(model);
    }

    @Test
    void when_save_withExistingNit_then_throwElementAlreadyExistsException() {
        // Arrange
        when(persistencePort.existsByNit(model.getNit())).thenReturn(true);

        // Act & Assert
        ElementAlreadyExistsException ex = assertThrows(ElementAlreadyExistsException.class, () -> useCase.save(model));
        assertEquals(String.format(DomainMessagesConstants.RESTAURANT_NIT_ALREADY_EXISTS, model.getNit()),
                ex.getMessage());
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(persistencePort, times(1)).existsByNit(model.getNit());
        verify(persistencePort, never()).save(any());
    }

    @Test
    void when_save_withInvalidRestaurant_then_throwValidationException() {
        // Arrange
        doThrow(new RuntimeException("Validation error")).when(validatorChain).validate(model, OperationType.CREATE);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.save(model));
        assertEquals("Validation error", ex.getMessage());
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(persistencePort, never()).existsByNit(any());
        verify(persistencePort, never()).save(any());
    }
}
