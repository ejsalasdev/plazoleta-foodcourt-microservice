package com.plazoleta.foodcourtmicroservice.commons.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.plazoleta.foodcourtmicroservice.domain.ports.in.DishServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.AuthenticatedUserPort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.usecases.DishUseCase;
import com.plazoleta.foodcourtmicroservice.domain.validation.dish.DishValidatorChain;
import com.plazoleta.foodcourtmicroservice.infrastructure.adapters.persistence.DishPersistenceAdapter;
import com.plazoleta.foodcourtmicroservice.infrastructure.mappers.DishEntityMapper;
import com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.DishRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DishBeanConfiguration {

    private final DishRepository dishRepository;
    private final DishEntityMapper dishEntityMapper;

    @Bean
    public DishPersistencePort dishPersistencePort() {
        return new DishPersistenceAdapter(dishRepository, dishEntityMapper);
    }

    @Bean
    public DishValidatorChain dishValidatorChain() {
        return new DishValidatorChain();
    }

    @Bean
    public DishServicePort dishServicePort(
            DishPersistencePort dishPersistencePort,
            DishValidatorChain dishValidatorChain,
            AuthenticatedUserPort authenticatedUserPort) {
        return new DishUseCase(dishPersistencePort, dishValidatorChain, authenticatedUserPort);
    }
}
