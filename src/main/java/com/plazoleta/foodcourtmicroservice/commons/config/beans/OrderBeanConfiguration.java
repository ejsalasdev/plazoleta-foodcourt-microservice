package com.plazoleta.foodcourtmicroservice.commons.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.plazoleta.foodcourtmicroservice.domain.ports.in.OrderServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.AuthenticatedUserPort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.OrderPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.UserServicePort;
import com.plazoleta.foodcourtmicroservice.domain.usecases.OrderUseCase;
import com.plazoleta.foodcourtmicroservice.infrastructure.adapters.persistence.OrderPersistenceAdapter;
import com.plazoleta.foodcourtmicroservice.infrastructure.mappers.OrderEntityMapper;
import com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.OrderRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class OrderBeanConfiguration {

    private final OrderRepository orderRepository;
    private final OrderEntityMapper orderEntityMapper;

    @Bean
    public OrderPersistencePort orderPersistencePort() {
        return new OrderPersistenceAdapter(orderRepository, orderEntityMapper);
    }

    @Bean
    public OrderServicePort orderServicePort(OrderPersistencePort orderPersistencePort,
            RestaurantPersistencePort restaurantPersistencePort,
            DishPersistencePort dishPersistencePort,
            AuthenticatedUserPort authenticatedUserPort,
            UserServicePort userServicePort) {
        return new OrderUseCase(orderPersistencePort,
                restaurantPersistencePort,
                dishPersistencePort,
                authenticatedUserPort,
                userServicePort);
    }
}
