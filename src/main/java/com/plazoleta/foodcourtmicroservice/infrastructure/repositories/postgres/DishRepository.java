package com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres;

import com.plazoleta.foodcourtmicroservice.infrastructure.entities.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DishRepository extends JpaRepository<DishEntity, Long>, JpaSpecificationExecutor<DishEntity> {
}
