package com.plazoleta.foodcourtmicroservice.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementNotFoundException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.dish.DishValidatorChain;

class DishUseCaseTest {

    private RestaurantModel buildRestaurant(Long id) {
        return new RestaurantModel(id, "Restaurante Prueba", "NIT123", "Calle 1", "123456789",
                "https://logo.com/logo.jpg", 1L);
    }

    @Mock
    private DishPersistencePort persistencePort;
    @Mock
    private DishValidatorChain validatorChain;

    @InjectMocks
    private DishUseCase useCase;

    private DishModel model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        model = new DishModel(1L, "Hamburguesa", new java.math.BigDecimal("15000.00"), "Clásica hamburguesa",
                "https://img.com/hamburguesa.jpg", 2L, buildRestaurant(10L), true);
    }

    @Test
    void when_save_withValidDish_then_dishIsSaved() {
        // Arrange
        when(persistencePort.existsByNameAndRestaurantId(model.getName(), model.getRestaurant().getId()))
                .thenReturn(false);

        // Act
        useCase.save(model);

        // Assert
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(persistencePort, times(1)).existsByNameAndRestaurantId(model.getName(), model.getRestaurant().getId());
        verify(persistencePort, times(1)).save(model);
    }

    @Test
    void when_save_withExistingDishName_then_throwElementAlreadyExistsException() {
        // Arrange
        when(persistencePort.existsByNameAndRestaurantId(model.getName(), model.getRestaurant().getId()))
                .thenReturn(true);

        // Act & Assert
        ElementAlreadyExistsException ex = assertThrows(ElementAlreadyExistsException.class, () -> useCase.save(model));
        assertEquals(String.format(DomainMessagesConstants.DISH_ALREADY_EXISTS, model.getName()), ex.getMessage());
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(persistencePort, times(1)).existsByNameAndRestaurantId(model.getName(), model.getRestaurant().getId());
        verify(persistencePort, never()).save(any());
    }

    @Test
    void when_save_withInvalidDish_then_throwValidationException() {
        // Arrange
        doThrow(new RuntimeException("Validation error")).when(validatorChain).validate(model, OperationType.CREATE);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.save(model));
        assertEquals("Validation error", ex.getMessage());
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(persistencePort, never()).existsByNameAndRestaurantId(any(), any());
        verify(persistencePort, never()).save(any());
    }

    @Test
    void when_updateDish_withValidData_then_dishIsUpdated() {
        // Arrange
        Long dishId = 1L;
        Long restaurantId = 10L;
        java.math.BigDecimal price = new java.math.BigDecimal("20000.00");
        String description = "Nueva descripción";
        when(persistencePort.existsByIdAndRestaurantId(dishId, restaurantId)).thenReturn(true);

        // Act
        useCase.updateDish(dishId, restaurantId, price, description);

        // Assert
        verify(persistencePort, times(1)).existsByIdAndRestaurantId(dishId, restaurantId);
        verify(validatorChain, times(1)).validate(any(DishModel.class), eq(OperationType.UPDATE));
        verify(persistencePort, times(1)).updateDish(dishId, restaurantId, price, description);
    }

    @Test
    void when_updateDish_dishNotFound_then_throwElementNotFoundException() {
        // Arrange
        Long dishId = 1L;
        Long restaurantId = 10L;
        java.math.BigDecimal price = new java.math.BigDecimal("20000.00");
        String description = "Nueva descripción";
        when(persistencePort.existsByIdAndRestaurantId(dishId, restaurantId)).thenReturn(false);

        // Act & Assert
        ElementNotFoundException ex = assertThrows(ElementNotFoundException.class, () ->
            useCase.updateDish(dishId, restaurantId, price, description)
        );
        assertEquals(String.format(DomainMessagesConstants.DISH_NOT_FOUND_IN_RESTAURANT, dishId, restaurantId), ex.getMessage());
        verify(persistencePort, times(1)).existsByIdAndRestaurantId(dishId, restaurantId);
        verify(validatorChain, never()).validate(any(DishModel.class), any());
        verify(persistencePort, never()).updateDish(any(), any(), any(), any());
    }

    @Test
    void when_updateDish_validationFails_then_throwValidationException() {
        // Arrange
        Long dishId = 1L;
        Long restaurantId = 10L;
        java.math.BigDecimal price = new java.math.BigDecimal("20000.00");
        String description = "Nueva descripción";
        when(persistencePort.existsByIdAndRestaurantId(dishId, restaurantId)).thenReturn(true);
        doThrow(new RuntimeException("Validation error")).when(validatorChain).validate(any(DishModel.class), eq(OperationType.UPDATE));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            useCase.updateDish(dishId, restaurantId, price, description)
        );
        assertEquals("Validation error", ex.getMessage());
        verify(persistencePort, times(1)).existsByIdAndRestaurantId(dishId, restaurantId);
        verify(validatorChain, times(1)).validate(any(DishModel.class), eq(OperationType.UPDATE));
        verify(persistencePort, never()).updateDish(any(), any(), any(), any());
    }
}
