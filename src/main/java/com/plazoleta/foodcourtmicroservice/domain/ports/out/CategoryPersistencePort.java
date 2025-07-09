package com.plazoleta.foodcourtmicroservice.domain.ports.out;

public interface CategoryPersistencePort {

    boolean existsById(Long id);
}
