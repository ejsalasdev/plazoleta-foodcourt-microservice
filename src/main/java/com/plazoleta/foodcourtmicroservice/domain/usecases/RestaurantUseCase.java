package com.plazoleta.foodcourtmicroservice.domain.usecases;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementAlreadyExistsException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidOwnerException;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.RestaurantServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.UserServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.AuthenticatedUserPort;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.UnauthorizedOperationException;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;
import com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.RestaurantValidatorChain;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.PaginationValidatorChain;
import java.util.Set;

public class RestaurantUseCase implements RestaurantServicePort {

    private final RestaurantPersistencePort restaurantPersistencePort;
    private final RestaurantValidatorChain restaurantValidatorChain;
    private final UserServicePort userServicePort;
    private final AuthenticatedUserPort authenticatedUserPort;
    private final PaginationValidatorChain paginationValidatorChain;

    public RestaurantUseCase(RestaurantPersistencePort restaurantPersistencePort,
                             RestaurantValidatorChain restaurantValidatorChain,
                             UserServicePort userServicePort,
                             AuthenticatedUserPort authenticatedUserPort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantValidatorChain = restaurantValidatorChain;
        this.userServicePort = userServicePort;
        this.authenticatedUserPort = authenticatedUserPort;
        // Only allow sorting by name for restaurants
        this.paginationValidatorChain = new PaginationValidatorChain(Set.of("name"));
    }

    @Override
    public void save(RestaurantModel restaurantModel) {
        restaurantValidatorChain.validate(restaurantModel, OperationType.CREATE);

        boolean isAdmin = authenticatedUserPort.getCurrentUserRoles().stream()
                .anyMatch("ADMIN"::equalsIgnoreCase);
        if (!isAdmin) {
            throw new UnauthorizedOperationException(
                    DomainMessagesConstants.USER_NOT_AUTHORIZED_TO_CREATE_RESTAURANT);
        }

        String ownerRole = userServicePort.getUserRoleById(restaurantModel.getOwnerId());
        if (!"OWNER".equalsIgnoreCase(ownerRole)) {
            throw new InvalidOwnerException(
                    String.format(DomainMessagesConstants.USER_IS_NOT_OWNER, restaurantModel.getOwnerId()));
        }

        boolean exists = restaurantPersistencePort.existsByNit(restaurantModel.getNit());
        if (exists) {
            throw new ElementAlreadyExistsException(
                    String.format(DomainMessagesConstants.RESTAURANT_NIT_ALREADY_EXISTS, restaurantModel.getNit()));
        }

        restaurantPersistencePort.save(restaurantModel);
    }

    @Override
    public PageInfo<RestaurantModel> findAll(Integer page, Integer size, String sortBy, boolean orderAsc) {
        paginationValidatorChain.validate(page, size, sortBy, orderAsc);
        return restaurantPersistencePort.findAll(page, size, sortBy, orderAsc);
    }

}
