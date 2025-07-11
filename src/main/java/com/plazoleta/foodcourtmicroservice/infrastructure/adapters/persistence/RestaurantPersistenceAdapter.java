package com.plazoleta.foodcourtmicroservice.infrastructure.adapters.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;
import com.plazoleta.foodcourtmicroservice.infrastructure.entities.RestaurantEntity;
import com.plazoleta.foodcourtmicroservice.infrastructure.mappers.RestaurantEntityMapper;
import com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantPersistenceAdapter implements RestaurantPersistencePort {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;

    @Override
    public void save(RestaurantModel restaurant) {
        restaurantRepository.save(restaurantEntityMapper.modelToEntity(restaurant));
    }

    @Override
    public boolean existsByNit(String nit) {
        return restaurantRepository.existsByNit(nit);
    }

    @Override
    public PageInfo<RestaurantModel> findAll(Integer page, Integer size, String sortBy, boolean orderAsc) {
        Sort sort  = Sort.by(sortBy);
        if (!orderAsc) {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RestaurantEntity> restaurantEntityPage = restaurantRepository.findAll(pageable);

        List<RestaurantModel> restaurantModels = restaurantEntityPage.getContent().stream()
                .map(restaurantEntityMapper::entityToModel)
                .toList();

        return new PageInfo<>(
                restaurantModels,
                restaurantEntityPage.getTotalElements(),
                restaurantEntityPage.getTotalPages(),
                restaurantEntityPage.getNumber(),
                restaurantEntityPage.getSize(),
                restaurantEntityPage.hasNext(),
                restaurantEntityPage.hasPrevious()
        );
    }

}
