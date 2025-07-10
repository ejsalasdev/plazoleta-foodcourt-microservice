package com.plazoleta.foodcourtmicroservice.domain.ports.out;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;

import java.util.Optional;

public interface RestaurantPersistencePort {

    void save(RestaurantModel restaurant);

    boolean existsByNit(String nit);

    boolean existsById(Long id);

    Optional<RestaurantModel> findRestaurantById(Long id);

    PageInfo<RestaurantModel> findAll(Integer page, Integer size, String sortBy, boolean orderAsc);
    
    Optional<RestaurantModel> findRestaurantByOwnerId(Long ownerId);
}
