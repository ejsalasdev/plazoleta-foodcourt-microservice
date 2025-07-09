package com.plazoleta.foodcourtmicroservice.domain.ports.out;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;

public interface RestaurantPersistencePort {

    void save(RestaurantModel restaurant);

    boolean existsByNit(String nit);

    boolean existsById(Long id);

    PageInfo<RestaurantModel> findAll(Integer page, Integer size, String sortBy, boolean orderAsc);
}
