package com.plazoleta.foodcourtmicroservice.domain.usecases;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.RestaurantServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;

public class RestaurantUseCase implements RestaurantServicePort {

    private final RestaurantPersistencePort restaurantPersistencePort;

    public RestaurantUseCase(RestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void save(RestaurantModel restaurantModel) {
        boolean exists = restaurantPersistencePort.existsByNit(restaurantModel.getNit());
        if (exists) {
            throw new IllegalArgumentException("Restaurant with NIT " + restaurantModel.getNit() + " already exists.");
        }

        restaurantPersistencePort.save(restaurantModel);
    }

}
