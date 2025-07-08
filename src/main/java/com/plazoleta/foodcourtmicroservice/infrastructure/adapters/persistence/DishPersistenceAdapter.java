package com.plazoleta.foodcourtmicroservice.infrastructure.adapters.persistence;

import java.math.BigDecimal;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.infrastructure.mappers.DishEntityMapper;
import com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.DishRepository;
import com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.specifications.DishSpecifications;

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

    @Override
    public void updateDish(Long dishId, Long restaurantId, BigDecimal price, String description) {
        dishRepository.findById(dishId).ifPresent(dish -> {
            dish.setPrice(price);
            dish.setDescription(description);
            dishRepository.save(dish);
        });
    }

    @Override
    public boolean existsByIdAndRestaurantId(Long dishId, Long restaurantId) {
        return dishRepository.count(
            DishSpecifications.idEqualsAndRestaurantId(dishId, restaurantId)
        ) > 0;
    }

    @Override
    public void setDishActive(Long dishId, Long restaurantId, boolean active) {
        dishRepository.findById(dishId).ifPresent(dish -> {
            if (dish.getRestaurant().getId().equals(restaurantId)) {
                dish.setActive(active);
                dishRepository.save(dish);
            }
        });
    }
}
