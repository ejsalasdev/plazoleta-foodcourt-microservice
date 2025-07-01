package com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres;

import com.plazoleta.foodcourtmicroservice.infrastructure.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    boolean existsByNit(String nit);
}
