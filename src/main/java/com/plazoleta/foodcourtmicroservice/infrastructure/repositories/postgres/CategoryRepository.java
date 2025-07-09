package com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plazoleta.foodcourtmicroservice.infrastructure.entities.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
