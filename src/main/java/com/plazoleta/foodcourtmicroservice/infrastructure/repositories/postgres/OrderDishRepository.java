package com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plazoleta.foodcourtmicroservice.infrastructure.entities.OrderDishEntity;

@Repository
public interface OrderDishRepository extends JpaRepository<OrderDishEntity, Long> {
}
