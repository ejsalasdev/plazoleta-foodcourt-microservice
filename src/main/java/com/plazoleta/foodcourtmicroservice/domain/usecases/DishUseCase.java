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
import com.plazoleta.foodcourtmicroservice.domain.ports.out.CategoryPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;
import com.plazoleta.foodcourtmicroservice.domain.validation.dish.DishValidatorChain;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.PaginationValidatorChain;

public class DishUseCase implements DishServicePort {

    private final DishPersistencePort dishPersistencePort;
    private final DishValidatorChain dishValidatorChain;
    private final AuthenticatedUserPort authenticatedUserPort;
    private final PaginationValidatorChain paginationValidatorChain;
    private final RestaurantPersistencePort restaurantPersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;

    public DishUseCase(DishPersistencePort dishPersistencePort,
            DishValidatorChain dishValidatorChain,
            AuthenticatedUserPort authenticatedUserPort,
            PaginationValidatorChain paginationValidatorChain,
            RestaurantPersistencePort restaurantPersistencePort,
            CategoryPersistencePort categoryPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.dishValidatorChain = dishValidatorChain;
        this.authenticatedUserPort = authenticatedUserPort;
        this.paginationValidatorChain = paginationValidatorChain;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public void save(DishModel dishModel) {

        List<String> currentUserRoles = authenticatedUserPort.getCurrentUserRoles();

        if (currentUserRoles.isEmpty() || !currentUserRoles.contains(DomainMessagesConstants.OWNER_ROLE)) {
            throw new ElementNotFoundException(
                    DomainMessagesConstants.USER_NOT_AUTHORIZED_TO_CREATE_DISH);
        }

        Long currentUserId = authenticatedUserPort.getCurrentUserId();
        Long restaurantId = dishModel.getRestaurant().getId();

        if (!dishPersistencePort.existsByRestaurantIdAndOwnerId(restaurantId, currentUserId)) {
            throw new UnauthorizedOperationException(DomainMessagesConstants.USER_NOT_OWNER_OF_RESTAURANT);
        }

        if (!categoryPersistencePort.existsById(dishModel.getCategory().getId())) {
            throw new ElementNotFoundException(
                    String.format(DomainMessagesConstants.CATEGORY_NOT_FOUND, dishModel.getCategory().getId()));
        }

        dishValidatorChain.validate(dishModel, OperationType.CREATE);

        if (dishPersistencePort.existsByNameAndRestaurantId(dishModel.getName(), restaurantId)) {
            throw new ElementAlreadyExistsException(
                    String.format(DomainMessagesConstants.DISH_ALREADY_EXISTS, dishModel.getName()));
        }

        dishPersistencePort.save(dishModel);
    }

    @Override
    public void updateDish(Long dishId, Long restaurantId, BigDecimal price, String description) {
        List<String> currentUserRoles = authenticatedUserPort.getCurrentUserRoles();
        if (currentUserRoles.isEmpty() || !currentUserRoles.contains(DomainMessagesConstants.OWNER_ROLE)) {
            throw new UnauthorizedOperationException(
                    DomainMessagesConstants.USER_NOT_AUTHORIZED_TO_UPDATE_DISH);
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

        if (currentUserRoles.isEmpty() || !currentUserRoles.contains(DomainMessagesConstants.OWNER_ROLE)) {
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

    @Override
    public PageInfo<DishModel> findAllByRestaurantId(Long restaurantId, Long categoryId, Integer page, Integer size,
            String sortBy, boolean orderAsc) {
        paginationValidatorChain.validate(page, size, sortBy, orderAsc);

        if (!categoryPersistencePort.existsById(categoryId)) {
            throw new ElementNotFoundException(
                    String.format(DomainMessagesConstants.CATEGORY_NOT_FOUND, categoryId));
        }

        if (!restaurantPersistencePort.existsById(restaurantId)) {
            throw new ElementNotFoundException(
                    String.format(DomainMessagesConstants.RESTAURANT_NOT_FOUND, restaurantId));
        }
        return dishPersistencePort.findAllByRestaurantId(restaurantId, categoryId, page, size, sortBy, orderAsc);
    }
}
