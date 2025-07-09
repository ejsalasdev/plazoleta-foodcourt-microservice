package com.plazoleta.foodcourtmicroservice.infrastructure.adapters.persistence;

import org.springframework.stereotype.Repository;

import com.plazoleta.foodcourtmicroservice.domain.ports.out.CategoryPersistencePort;
import com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPersistencePort {

    private final CategoryRepository categoryRepository;

    @Override
    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }
}
