package com.plazoleta.foodcourtmicroservice.commons.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.plazoleta.foodcourtmicroservice.domain.ports.in.RestaurantServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.usecases.RestaurantUseCase;
import com.plazoleta.foodcourtmicroservice.domain.validation.RestaurantValidatorChain;
import com.plazoleta.foodcourtmicroservice.infrastructure.adapters.persistence.RestaurantPersistenceAdapter;
import com.plazoleta.foodcourtmicroservice.infrastructure.mappers.RestaurantEntityMapper;
import com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RestaurantBeanConfiguration {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;

    @Bean
    public RestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantPersistenceAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public RestaurantValidatorChain restaurantValidatorChain() {
        return new RestaurantValidatorChain();
    }

    @Bean
    public RestaurantServicePort restaurantServicePort(
            RestaurantPersistencePort restaurantPersistencePort,
            RestaurantValidatorChain restaurantValidatorChain) {
        return new RestaurantUseCase(
                restaurantPersistencePort, restaurantValidatorChain);
    }

}
