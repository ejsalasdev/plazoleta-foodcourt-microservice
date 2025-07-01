package com.plazoleta.foodcourtmicroservice.domain.usecases;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.RestaurantServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;

import com.plazoleta.foodcourtmicroservice.domain.validation.RestaurantValidatorChain;

public class RestaurantUseCase implements RestaurantServicePort {

    private final RestaurantPersistencePort restaurantPersistencePort;
    private final RestaurantValidatorChain restaurantValidatorChain;

    public RestaurantUseCase(RestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantValidatorChain = new RestaurantValidatorChain();
    }

    @Override
    public void save(RestaurantModel restaurantModel) {
        // Validar datos del restaurante
        restaurantValidatorChain.validate(restaurantModel);

        boolean exists = restaurantPersistencePort.existsByNit(restaurantModel.getNit());
        if (exists) {
            throw new IllegalArgumentException("Restaurant with NIT " + restaurantModel.getNit() + " already exists.");
        }

        restaurantPersistencePort.save(restaurantModel);
    }

}
