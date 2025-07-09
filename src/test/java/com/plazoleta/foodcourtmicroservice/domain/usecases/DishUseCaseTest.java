package com.plazoleta.foodcourtmicroservice.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementAlreadyExistsException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementNotFoundException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.UnauthorizedOperationException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.AuthenticatedUserPort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.validation.dish.DishValidatorChain;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.PaginationValidatorChain;

class DishUseCaseTest {

    private RestaurantModel buildRestaurant(Long id) {
        return new RestaurantModel(id, "Restaurante Prueba", "NIT123", "Calle 1", "123456789",
                "https://logo.com/logo.jpg", 1L);
    }


    @Mock
    private DishPersistencePort persistencePort;

    @Mock
    private DishValidatorChain validatorChain;

    @Mock
    private AuthenticatedUserPort authenticatedUserPort;

    @Mock
    private PaginationValidatorChain paginationValidatorChain;

    @Mock
    private RestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private DishUseCase useCase;

    private DishModel model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        model = new DishModel(1L, "Hamburguesa", new BigDecimal("15000.00"), "Clásica hamburguesa",
                "https://img.com/hamburguesa.jpg", 2L, buildRestaurant(10L), true);
        useCase = new DishUseCase(persistencePort, validatorChain, authenticatedUserPort, paginationValidatorChain, restaurantPersistencePort);
    }

    @Test
    void when_setDishActive_userNotOwnerRole_then_throwUnauthorizedOperationException() {
        // Arrange
        Long dishId = 1L;
        Long restaurantId = 10L;
        boolean active = true;
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(Arrays.asList("EMPLOYEE"));

        // Act & Assert
        UnauthorizedOperationException ex = assertThrows(
                UnauthorizedOperationException.class,
                () -> useCase.setDishActive(dishId, restaurantId, active));
        assertEquals(
                DomainMessagesConstants.USER_NOT_AUTHORIZED_TO_ENABLE_DISABLE_DISH,
                ex.getMessage());
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(authenticatedUserPort, never()).getCurrentUserId();
        verify(persistencePort, never()).existsByRestaurantIdAndOwnerId(anyLong(), anyLong());
        verify(persistencePort, never()).existsByIdAndRestaurantId(anyLong(), anyLong());
        verify(persistencePort, never()).setDishActive(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    void when_setDishActive_userNotOwnerOfRestaurant_then_throwUnauthorizedOperationException() {
        // Arrange
        Long dishId = 1L;
        Long restaurantId = 10L;
        boolean active = false;
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(Arrays.asList("OWNER"));
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(99L);
        when(persistencePort.existsByRestaurantIdAndOwnerId(restaurantId, 99L)).thenReturn(false);

        // Act & Assert
        UnauthorizedOperationException ex = assertThrows(
                UnauthorizedOperationException.class,
                () -> useCase.setDishActive(dishId, restaurantId, active));
        assertEquals(
                DomainMessagesConstants.USER_NOT_OWNER_OF_RESTAURANT,
                ex.getMessage());
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(authenticatedUserPort, times(1)).getCurrentUserId();
        verify(persistencePort, times(1)).existsByRestaurantIdAndOwnerId(restaurantId, 99L);
        verify(persistencePort, never()).existsByIdAndRestaurantId(anyLong(), anyLong());
        verify(persistencePort, never()).setDishActive(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    void when_setDishActive_dishNotFound_then_throwElementNotFoundException() {
        // Arrange
        Long dishId = 1L;
        Long restaurantId = 10L;
        boolean active = false;
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(Arrays.asList("OWNER"));
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(1L);
        when(persistencePort.existsByRestaurantIdAndOwnerId(restaurantId, 1L)).thenReturn(true);
        when(persistencePort.existsByIdAndRestaurantId(dishId, restaurantId)).thenReturn(false);

        // Act & Assert
        ElementNotFoundException ex = assertThrows(
                ElementNotFoundException.class,
                () -> useCase.setDishActive(dishId, restaurantId, active));
        assertEquals(String.format(
                DomainMessagesConstants.DISH_NOT_FOUND_IN_RESTAURANT,
                dishId, restaurantId), ex.getMessage());
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(authenticatedUserPort, times(1)).getCurrentUserId();
        verify(persistencePort, times(1)).existsByRestaurantIdAndOwnerId(restaurantId, 1L);
        verify(persistencePort, times(1)).existsByIdAndRestaurantId(dishId, restaurantId);
        verify(persistencePort, never()).setDishActive(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    void when_setDishActive_validRequest_then_setDishActiveCalled() {
        // Arrange
        Long dishId = 1L;
        Long restaurantId = 10L;
        boolean active = true;
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(Arrays.asList("OWNER"));
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(1L);
        when(persistencePort.existsByRestaurantIdAndOwnerId(restaurantId, 1L)).thenReturn(true);
        when(persistencePort.existsByIdAndRestaurantId(dishId, restaurantId)).thenReturn(true);

        // Act
        useCase.setDishActive(dishId, restaurantId, active);

        // Assert
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(authenticatedUserPort, times(1)).getCurrentUserId();
        verify(persistencePort, times(1)).existsByRestaurantIdAndOwnerId(restaurantId, 1L);
        verify(persistencePort, times(1)).existsByIdAndRestaurantId(dishId, restaurantId);
        verify(persistencePort, times(1)).setDishActive(dishId, restaurantId, active);
    }

    @Test
    void when_save_withValidDishAndOwnerOfRestaurant_then_dishIsSaved() {
        // Arrange
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(Collections.singletonList("OWNER"));
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(1L);
        when(persistencePort.existsByRestaurantIdAndOwnerId(model.getRestaurant().getId(), 1L)).thenReturn(true);
        when(persistencePort.existsByNameAndRestaurantId(model.getName(), model.getRestaurant().getId()))
                .thenReturn(false);

        // Act
        useCase.save(model);

        // Assert
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(authenticatedUserPort, times(1)).getCurrentUserId();
        verify(persistencePort, times(1)).existsByRestaurantIdAndOwnerId(model.getRestaurant().getId(), 1L);
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(persistencePort, times(1)).existsByNameAndRestaurantId(model.getName(), model.getRestaurant().getId());
        verify(persistencePort, times(1)).save(model);
    }

    @Test
    void when_save_withOwnerRoleButNotOwnerOfRestaurant_then_throwUnauthorizedOperationException() {
        // Arrange
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(Collections.singletonList("OWNER"));
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(2L);
        when(persistencePort.existsByRestaurantIdAndOwnerId(model.getRestaurant().getId(), 2L)).thenReturn(false);

        // Act & Assert
        UnauthorizedOperationException ex = assertThrows(UnauthorizedOperationException.class,
                () -> useCase.save(model));
        assertEquals(DomainMessagesConstants.USER_NOT_OWNER_OF_RESTAURANT, ex.getMessage());
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(authenticatedUserPort, times(1)).getCurrentUserId();
        verify(persistencePort, times(1)).existsByRestaurantIdAndOwnerId(model.getRestaurant().getId(), 2L);
        verify(validatorChain, never()).validate(any(), any());
        verify(persistencePort, never()).existsByNameAndRestaurantId(any(), anyLong());
        verify(persistencePort, never()).save(any());
    }

    @Test
    void when_save_withExistingDishName_then_throwElementAlreadyExistsException() {
        // Arrange
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(Collections.singletonList("OWNER"));
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(1L);
        when(persistencePort.existsByRestaurantIdAndOwnerId(model.getRestaurant().getId(), 1L)).thenReturn(true);
        when(persistencePort.existsByNameAndRestaurantId(model.getName(), model.getRestaurant().getId()))
                .thenReturn(true);

        // Act & Assert
        ElementAlreadyExistsException ex = assertThrows(ElementAlreadyExistsException.class, () -> useCase.save(model));
        assertEquals(String.format(DomainMessagesConstants.DISH_ALREADY_EXISTS, model.getName()), ex.getMessage());
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(authenticatedUserPort, times(1)).getCurrentUserId();
        verify(persistencePort, times(1)).existsByRestaurantIdAndOwnerId(model.getRestaurant().getId(), 1L);
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(persistencePort, times(1)).existsByNameAndRestaurantId(model.getName(), model.getRestaurant().getId());
        verify(persistencePort, never()).save(any());
    }

    @Test
    void when_save_withInvalidDish_then_throwValidationException() {
        // Arrange
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(Collections.singletonList("OWNER"));
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(1L);
        when(persistencePort.existsByRestaurantIdAndOwnerId(model.getRestaurant().getId(), 1L)).thenReturn(true);
        doThrow(new RuntimeException("Validation error")).when(validatorChain).validate(model, OperationType.CREATE);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.save(model));
        assertEquals("Validation error", ex.getMessage());
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(authenticatedUserPort, times(1)).getCurrentUserId();
        verify(persistencePort, times(1)).existsByRestaurantIdAndOwnerId(model.getRestaurant().getId(), 1L);
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(persistencePort, never()).existsByNameAndRestaurantId(any(), anyLong());
        verify(persistencePort, never()).save(any());
    }

    @Test
    void when_save_withNonOwnerRole_then_throwElementNotFoundException() {
        // Arrange
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(Collections.singletonList("EMPLOYEE"));

        // Act & Assert
        ElementNotFoundException ex = assertThrows(ElementNotFoundException.class, () -> useCase.save(model));
        assertEquals(DomainMessagesConstants.USER_NOT_AUTHORIZED_TO_CREATE_DISH, ex.getMessage());
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(validatorChain, never()).validate(any(), any());
        verify(persistencePort, never()).existsByNameAndRestaurantId(any(), anyLong());
        verify(persistencePort, never()).save(any());
    }

    @Test

    void when_updateDish_withValidData_then_dishIsUpdated() {
        // Arrange
        Long dishId = 1L;
        Long restaurantId = 10L;
        BigDecimal price = new BigDecimal("20000.00");
        String description = "Nueva descripción";
        when(authenticatedUserPort.getCurrentUserRoles())
                .thenReturn(Collections.singletonList(DomainMessagesConstants.OWNER_ROLE));
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(1L);
        when(persistencePort.existsByRestaurantIdAndOwnerId(restaurantId, 1L)).thenReturn(true);
        when(persistencePort.existsByIdAndRestaurantId(dishId, restaurantId)).thenReturn(true);

        // Act
        useCase.updateDish(dishId, restaurantId, price, description);

        // Assert
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(authenticatedUserPort, times(1)).getCurrentUserId();
        verify(persistencePort, times(1)).existsByRestaurantIdAndOwnerId(restaurantId, 1L);
        verify(persistencePort, times(1)).existsByIdAndRestaurantId(dishId, restaurantId);
        verify(validatorChain, times(1)).validate(any(DishModel.class), eq(OperationType.UPDATE));
        verify(persistencePort, times(1)).updateDish(dishId, restaurantId, price, description);
    }

    @Test

    void when_updateDish_dishNotFound_then_throwElementNotFoundException() {
        // Arrange
        Long dishId = 1L;
        Long restaurantId = 10L;
        BigDecimal price = new BigDecimal("20000.00");
        String description = "Nueva descripción";
        when(authenticatedUserPort.getCurrentUserRoles())
                .thenReturn(Collections.singletonList(DomainMessagesConstants.OWNER_ROLE));
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(1L);
        when(persistencePort.existsByRestaurantIdAndOwnerId(restaurantId, 1L)).thenReturn(true);
        when(persistencePort.existsByIdAndRestaurantId(dishId, restaurantId)).thenReturn(false);

        // Act & Assert
        ElementNotFoundException ex = assertThrows(ElementNotFoundException.class,
                () -> useCase.updateDish(dishId, restaurantId, price, description));
        assertEquals(String.format(DomainMessagesConstants.DISH_NOT_FOUND_IN_RESTAURANT, dishId, restaurantId),
                ex.getMessage());
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(authenticatedUserPort, times(1)).getCurrentUserId();
        verify(persistencePort, times(1)).existsByRestaurantIdAndOwnerId(restaurantId, 1L);
        verify(persistencePort, times(1)).existsByIdAndRestaurantId(dishId, restaurantId);
        verify(validatorChain, never()).validate(any(DishModel.class), any());
        verify(persistencePort, never()).updateDish(anyLong(), anyLong(), any(), any());
    }

    @Test

    void when_updateDish_validationFails_then_throwValidationException() {
        // Arrange
        Long dishId = 1L;
        Long restaurantId = 10L;
        BigDecimal price = new BigDecimal("20000.00");
        String description = "Nueva descripción";
        when(authenticatedUserPort.getCurrentUserRoles())
                .thenReturn(Collections.singletonList(DomainMessagesConstants.OWNER_ROLE));
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(1L);
        when(persistencePort.existsByRestaurantIdAndOwnerId(restaurantId, 1L)).thenReturn(true);
        when(persistencePort.existsByIdAndRestaurantId(dishId, restaurantId)).thenReturn(true);
        doThrow(new RuntimeException("Validation error")).when(validatorChain).validate(any(DishModel.class),
                eq(OperationType.UPDATE));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> useCase.updateDish(dishId, restaurantId, price, description));
        assertEquals("Validation error", ex.getMessage());
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(authenticatedUserPort, times(1)).getCurrentUserId();
        verify(persistencePort, times(1)).existsByRestaurantIdAndOwnerId(restaurantId, 1L);
        verify(persistencePort, times(1)).existsByIdAndRestaurantId(dishId, restaurantId);
        verify(validatorChain, times(1)).validate(any(DishModel.class), eq(OperationType.UPDATE));
        verify(persistencePort, never()).updateDish(anyLong(), anyLong(), any(), any());
    }

    @Test
    void when_updateDish_userNotOwnerRole_then_throwUnauthorizedOperationException() {
        // Arrange
        Long dishId = 1L;
        Long restaurantId = 10L;
        BigDecimal price = new BigDecimal("20000.00");
        String description = "Nueva descripción";
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(Collections.singletonList("EMPLOYEE"));

        // Act & Assert
        UnauthorizedOperationException ex = assertThrows(UnauthorizedOperationException.class,
                () -> useCase.updateDish(dishId, restaurantId, price, description));
        assertEquals(DomainMessagesConstants.USER_NOT_AUTHORIZED_TO_UPDATE_DISH, ex.getMessage());
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(authenticatedUserPort, never()).getCurrentUserId();
        verify(persistencePort, never()).existsByRestaurantIdAndOwnerId(anyLong(), anyLong());
        verify(persistencePort, never()).existsByIdAndRestaurantId(anyLong(), anyLong());
        verify(validatorChain, never()).validate(any(DishModel.class), any());
        verify(persistencePort, never()).updateDish(anyLong(), anyLong(), any(), any());
    }

    @Test
    void when_updateDish_userNotOwnerOfRestaurant_then_throwUnauthorizedOperationException() {
        // Arrange
        Long dishId = 1L;
        Long restaurantId = 10L;
        BigDecimal price = new BigDecimal("20000.00");
        String description = "Nueva descripción";
        when(authenticatedUserPort.getCurrentUserRoles())
                .thenReturn(Collections.singletonList(DomainMessagesConstants.OWNER_ROLE));
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(99L);
        when(persistencePort.existsByRestaurantIdAndOwnerId(restaurantId, 99L)).thenReturn(false);

        // Act & Assert
        UnauthorizedOperationException ex = assertThrows(UnauthorizedOperationException.class,
                () -> useCase.updateDish(dishId, restaurantId, price, description));
        assertEquals(DomainMessagesConstants.USER_NOT_OWNER_OF_RESTAURANT, ex.getMessage());
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(authenticatedUserPort, times(1)).getCurrentUserId();
        verify(persistencePort, times(1)).existsByRestaurantIdAndOwnerId(restaurantId, 99L);
        verify(persistencePort, never()).existsByIdAndRestaurantId(anyLong(), anyLong());
        verify(validatorChain, never()).validate(any(DishModel.class), any());
        verify(persistencePort, never()).updateDish(anyLong(), anyLong(), any(), any());
    }
}
