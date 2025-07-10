package com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres;

import com.plazoleta.foodcourtmicroservice.infrastructure.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    boolean existsByNit(String nit);
    
    Optional<RestaurantEntity> findByOwnerId(Long ownerId);
}
