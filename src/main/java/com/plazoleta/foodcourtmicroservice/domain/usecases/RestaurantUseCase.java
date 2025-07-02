package com.plazoleta.foodcourtmicroservice.domain.usecases;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.RestaurantServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;


import com.plazoleta.foodcourtmicroservice.domain.validation.RestaurantValidatorChain;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementAlreadyExistsException;

public class RestaurantUseCase implements RestaurantServicePort {

    private final RestaurantPersistencePort restaurantPersistencePort;
    private final RestaurantValidatorChain restaurantValidatorChain;

    public RestaurantUseCase(RestaurantPersistencePort restaurantPersistencePort, RestaurantValidatorChain restaurantValidatorChain) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantValidatorChain = restaurantValidatorChain;
    }

    @Override
    public void save(RestaurantModel restaurantModel) {
        // Validar datos del restaurante
        restaurantValidatorChain.validate(restaurantModel);

        boolean exists = restaurantPersistencePort.existsByNit(restaurantModel.getNit());
        if (exists) {
            throw new ElementAlreadyExistsException("Restaurant with NIT " + restaurantModel.getNit() + " already exists.");
        }

        restaurantPersistencePort.save(restaurantModel);
    }

}
