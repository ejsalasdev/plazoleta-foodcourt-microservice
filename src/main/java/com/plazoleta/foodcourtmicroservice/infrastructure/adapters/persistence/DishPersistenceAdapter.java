package com.plazoleta.foodcourtmicroservice.infrastructure.adapters.persistence;

import org.springframework.stereotype.Component;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.infrastructure.mappers.DishEntityMapper;
import com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.DishRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DishPersistenceAdapter implements DishPersistencePort {

    private final DishRepository dishRepository;
    private final DishEntityMapper dishEntityMapper;

    @Override
    public void save(DishModel dishModel) {
        dishRepository.save(dishEntityMapper.modelToEntity(dishModel));
    }

    @Override
    public boolean existsByNameAndRestaurantId(String name, Long restaurantId) {
        return dishRepository.existsByNameAndRestaurantId(name, restaurantId);
    }
}
