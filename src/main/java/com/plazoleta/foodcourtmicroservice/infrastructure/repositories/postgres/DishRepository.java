package com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres;

import com.plazoleta.foodcourtmicroservice.infrastructure.entities.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<DishEntity, Long> {
    
    boolean existsByNameAndRestaurantId(String name, Long restaurantId);
}
