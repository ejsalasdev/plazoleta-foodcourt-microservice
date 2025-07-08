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
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidOwnerException;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.UserServicePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.AuthenticatedUserPort;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.UnauthorizedOperationException;
import com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.RestaurantValidatorChain;

class RestaurantUseCaseTest {

    @Mock
    private RestaurantPersistencePort persistencePort;

    @Mock
    private RestaurantValidatorChain validatorChain;

    @Mock
    private UserServicePort userServicePort;


    @Mock
    private AuthenticatedUserPort authenticatedUserPort;

    @InjectMocks
    private RestaurantUseCase useCase;

    private RestaurantModel model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        model = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "+573001234567", "logo.png", 10L);
        useCase = new RestaurantUseCase(persistencePort, validatorChain, userServicePort, authenticatedUserPort);
    }

    @Test
    void when_save_withValidRestaurantAndAdminUser_then_restaurantIsSaved() {
        // Arrange
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(java.util.Arrays.asList("ADMIN"));
        when(userServicePort.getUserRoleById(model.getOwnerId())).thenReturn("OWNER");
        when(persistencePort.existsByNit(model.getNit())).thenReturn(false);

        // Act
        useCase.save(model);

        // Assert
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(persistencePort, times(1)).existsByNit(model.getNit());
        verify(persistencePort, times(1)).save(model);
    }

    @Test
    void when_save_withNonAdminUser_then_throwUnauthorizedOperationException() {
        // Arrange
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(java.util.Arrays.asList("OWNER"));

        // Act & Assert
        UnauthorizedOperationException ex = assertThrows(UnauthorizedOperationException.class, () -> useCase.save(model));
        assertEquals(DomainMessagesConstants.USER_NOT_AUTHORIZED_TO_CREATE_RESTAURANT, ex.getMessage());
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(persistencePort, never()).existsByNit(any());
        verify(persistencePort, never()).save(any());
    }

    @Test
    void when_save_withInvalidOwner_then_throwInvalidOwnerException() {
        // Arrange
        doThrow(new InvalidOwnerException("Invalid owner")).when(validatorChain).validate(model, OperationType.CREATE);

        // Act & Assert
        InvalidOwnerException ex = assertThrows(InvalidOwnerException.class, () -> useCase.save(model));
        assertEquals("Invalid owner", ex.getMessage());
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(persistencePort, never()).existsByNit(any());
        verify(persistencePort, never()).save(any());
    }

    @Test
    void when_save_withNonOwnerRole_andAdminUser_then_throwInvalidOwnerException() {
        // Arrange
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(java.util.Arrays.asList("ADMIN"));
        when(userServicePort.getUserRoleById(model.getOwnerId())).thenReturn("ADMIN");

        // Act & Assert
        InvalidOwnerException ex = assertThrows(InvalidOwnerException.class, () -> useCase.save(model));
        assertEquals(String.format(DomainMessagesConstants.USER_IS_NOT_OWNER, model.getOwnerId()), ex.getMessage());
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(userServicePort, times(1)).getUserRoleById(model.getOwnerId());
        verify(persistencePort, never()).existsByNit(any());
        verify(persistencePort, never()).save(any());
    }

    @Test
    void when_save_withExistingNit_andAdminUser_then_throwElementAlreadyExistsException() {
        // Arrange
        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(java.util.Arrays.asList("ADMIN"));
        when(userServicePort.getUserRoleById(model.getOwnerId())).thenReturn("OWNER");
        when(persistencePort.existsByNit(model.getNit())).thenReturn(true);

        // Act & Assert
        ElementAlreadyExistsException ex = assertThrows(ElementAlreadyExistsException.class, () -> useCase.save(model));
        assertEquals(String.format(DomainMessagesConstants.RESTAURANT_NIT_ALREADY_EXISTS, model.getNit()),
                ex.getMessage());
        verify(validatorChain, times(1)).validate(model, OperationType.CREATE);
        verify(authenticatedUserPort, times(1)).getCurrentUserRoles();
        verify(userServicePort, times(1)).getUserRoleById(model.getOwnerId());
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
