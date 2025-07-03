package com.plazoleta.foodcourtmicroservice.infrastructure.adapters.persistence;


import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.infrastructure.mappers.DishEntityMapper;
import com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.DishRepository;
import com.plazoleta.foodcourtmicroservice.infrastructure.specifications.DishSpecifications;

import lombok.RequiredArgsConstructor;

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
        return dishRepository.count(
                DishSpecifications.nameEqualsIgnoreCaseAndRestaurantId(name, restaurantId)) > 0;
    }
}
