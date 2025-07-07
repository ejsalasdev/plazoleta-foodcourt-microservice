package com.plazoleta.foodcourtmicroservice.domain.usecases;

import com.plazoleta.foodcourtmicroservice.domain.enums.OperationType;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementAlreadyExistsException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidOwnerException;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.RestaurantServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.UserServicePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.restaurant.RestaurantValidatorChain;

public class RestaurantUseCase implements RestaurantServicePort {

    private final RestaurantPersistencePort restaurantPersistencePort;
    private final RestaurantValidatorChain restaurantValidatorChain;
    private final UserServicePort userServicePort;

    public RestaurantUseCase(RestaurantPersistencePort restaurantPersistencePort,
            RestaurantValidatorChain restaurantValidatorChain,
            UserServicePort userServicePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantValidatorChain = restaurantValidatorChain;
        this.userServicePort = userServicePort;
    }

    @Override
    public void save(RestaurantModel restaurantModel) {
        restaurantValidatorChain.validate(restaurantModel, OperationType.CREATE);

        String role = userServicePort.getUserRoleById(restaurantModel.getOwnerId());
        if (!"OWNER".equalsIgnoreCase(role)) {
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

}
