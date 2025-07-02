package com.plazoleta.foodcourtmicroservice.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementAlreadyExistsException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.dish.DishValidatorChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DishUseCaseTest {

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
        model = new DishModel(1L, "Hamburguesa", new java.math.BigDecimal("15000.00"), "Clásica hamburguesa", "https://img.com/hamburguesa.jpg", 2L, 10L, true);
    }

    @Test
    void when_save_withValidDish_then_dishIsSaved() {
        // Arrange
        when(persistencePort.existsByNameAndRestaurantId(model.getName(), model.getRestaurantId())).thenReturn(false);

        // Act
        useCase.save(model);

        // Assert
        verify(validatorChain, times(1)).validate(model);
        verify(persistencePort, times(1)).existsByNameAndRestaurantId(model.getName(), model.getRestaurantId());
        verify(persistencePort, times(1)).save(model);
    }

    @Test
    void when_save_withExistingDishName_then_throwElementAlreadyExistsException() {
        // Arrange
        when(persistencePort.existsByNameAndRestaurantId(model.getName(), model.getRestaurantId())).thenReturn(true);

        // Act & Assert
        ElementAlreadyExistsException ex = assertThrows(ElementAlreadyExistsException.class, () -> useCase.save(model));
        assertEquals(String.format(DomainMessagesConstants.DISH_ALREADY_EXISTS, model.getName()), ex.getMessage());
        verify(validatorChain, times(1)).validate(model);
        verify(persistencePort, times(1)).existsByNameAndRestaurantId(model.getName(), model.getRestaurantId());
        verify(persistencePort, never()).save(any());
    }

    @Test
    void when_save_withInvalidDish_then_throwValidationException() {
        // Arrange
        doThrow(new RuntimeException("Validation error")).when(validatorChain).validate(model);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.save(model));
        assertEquals("Validation error", ex.getMessage());
        verify(validatorChain, times(1)).validate(model);
        verify(persistencePort, never()).existsByNameAndRestaurantId(any(), any());
        verify(persistencePort, never()).save(any());
    }
}
