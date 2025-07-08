package com.plazoleta.foodcourtmicroservice.domain.usecases;

import java.math.BigDecimal;
import java.util.List;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementAlreadyExistsException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementNotFoundException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.UnauthorizedOperationException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.DishServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.AuthenticatedUserPort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.dish.DishValidatorChain;

public class DishUseCase implements DishServicePort {

    private final DishPersistencePort dishPersistencePort;
    private final DishValidatorChain dishValidatorChain;
    private final AuthenticatedUserPort authenticatedUserPort;

    public DishUseCase(DishPersistencePort dishPersistencePort,
            DishValidatorChain dishValidatorChain,
            AuthenticatedUserPort authenticatedUserPort) {
        this.dishPersistencePort = dishPersistencePort;
        this.dishValidatorChain = dishValidatorChain;
        this.authenticatedUserPort = authenticatedUserPort;
    }

    @Override
    public void save(DishModel dishModel) {

        List<String> currentUserRoles = authenticatedUserPort.getCurrentUserRoles();

        if (currentUserRoles.isEmpty() || !currentUserRoles.contains("OWNER")) {
            throw new ElementNotFoundException(
                    DomainMessagesConstants.USER_NOT_AUTHORIZED_TO_CREATE_DISH);
        }

        dishValidatorChain.validate(dishModel, OperationType.CREATE);

        Long restaurantId = dishModel.getRestaurant() != null ? dishModel.getRestaurant().getId() : null;
        if (dishPersistencePort.existsByNameAndRestaurantId(dishModel.getName(), restaurantId)) {
            throw new ElementAlreadyExistsException(
                    String.format(DomainMessagesConstants.DISH_ALREADY_EXISTS, dishModel.getName()));
        }

        dishPersistencePort.save(dishModel);
    }

    @Override
    public void updateDish(Long dishId, Long restaurantId, BigDecimal price, String description) {
        if (!dishPersistencePort.existsByIdAndRestaurantId(dishId, restaurantId)) {
            throw new ElementNotFoundException(
                    String.format(DomainMessagesConstants.DISH_NOT_FOUND_IN_RESTAURANT, dishId, restaurantId));
        }

        DishModel dishModel = new DishModel();
        dishModel.setId(dishId);
        dishModel.setPrice(price);
        dishModel.setDescription(description);
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);
        dishModel.setRestaurant(restaurant);

        dishValidatorChain.validate(dishModel, OperationType.UPDATE);

        dishPersistencePort.updateDish(dishId, restaurantId, price, description);
    }

    @Override
    public void setDishActive(Long dishId, Long restaurantId, boolean active) {
        List<String> currentUserRoles = authenticatedUserPort.getCurrentUserRoles();

        if (currentUserRoles.isEmpty() || !currentUserRoles.contains("OWNER")) {
            throw new UnauthorizedOperationException(
                    DomainMessagesConstants.USER_NOT_AUTHORIZED_TO_ENABLE_DISABLE_DISH);
        }

        Long currentUserId = authenticatedUserPort.getCurrentUserId();

        if (!dishPersistencePort.existsByRestaurantIdAndOwnerId(restaurantId, currentUserId)) {
            throw new UnauthorizedOperationException(
                    DomainMessagesConstants.USER_NOT_OWNER_OF_RESTAURANT);
        }

        if (!dishPersistencePort.existsByIdAndRestaurantId(dishId, restaurantId)) {
            throw new ElementNotFoundException(
                    String.format(DomainMessagesConstants.DISH_NOT_FOUND_IN_RESTAURANT, dishId, restaurantId));
        }
        dishPersistencePort.setDishActive(dishId, restaurantId, active);
    }
}
