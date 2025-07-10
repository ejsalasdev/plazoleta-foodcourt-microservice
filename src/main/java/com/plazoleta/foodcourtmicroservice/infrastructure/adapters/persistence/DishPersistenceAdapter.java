package com.plazoleta.foodcourtmicroservice.infrastructure.adapters.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;
import com.plazoleta.foodcourtmicroservice.infrastructure.entities.DishEntity;
import com.plazoleta.foodcourtmicroservice.infrastructure.mappers.DishEntityMapper;
import com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.DishRepository;
import com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.specifications.DishSpecifications;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DishPersistenceAdapter implements DishPersistencePort {

    private final DishRepository dishRepository;
    private final DishEntityMapper dishEntityMapper;

    @Override
    public void save(DishModel dishModel) {
        dishRepository.save(dishEntityMapper.modelToEntity(dishModel));
    }

    @Override
    public Optional<DishModel> findDishById(Long dishId) {
        return dishRepository.findById(dishId)
                .map(dishEntityMapper::entityToModel);
    }

    @Override
    public void updateDish(Long dishId, Long restaurantId, BigDecimal price, String description) {
        dishRepository.findById(dishId).ifPresent(dish -> {
            dish.setPrice(price);
            dish.setDescription(description);
            dishRepository.save(dish);
        });
    }

    @Override
    public void setDishActive(Long dishId, Long restaurantId, boolean active) {
        dishRepository.findById(dishId).ifPresent(dish -> {
            if (dish.getRestaurant().getId().equals(restaurantId)) {
                dish.setActive(active);
                dishRepository.save(dish);
            }
        });
    }

    @Override
    public PageInfo<DishModel> findAllByRestaurantId(Long restaurantId, Long categoryId, Integer page, Integer size,
            String sortBy, boolean orderAsc) {
        Sort sort = Sort.by(sortBy);
        if (!orderAsc) {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<DishEntity> specification = DishSpecifications.restaurantIdEquals(restaurantId);

        if (categoryId != null) {
            specification = specification.and(DishSpecifications.categoryIdEquals(categoryId));
        }

        Page<DishEntity> dishEntityPage = dishRepository.findAll(specification, pageable);

        List<DishModel> dishModels = dishEntityPage.getContent().stream()
                .map(dishEntityMapper::entityToModel)
                .toList();

        return new PageInfo<>(
                dishModels,
                dishEntityPage.getTotalElements(),
                dishEntityPage.getTotalPages(),
                dishEntityPage.getNumber(),
                dishEntityPage.getSize(),
                dishEntityPage.hasNext(),
                dishEntityPage.hasPrevious());
    }

    @Override
    public boolean existsByNameAndRestaurantId(String name, Long restaurantId) {
        return dishRepository.count(
                DishSpecifications.nameEqualsIgnoreCaseAndRestaurantId(name, restaurantId)) > 0;
    }

    @Override
    public boolean existsByIdAndRestaurantId(Long dishId, Long restaurantId) {
        return dishRepository.count(
                DishSpecifications.idEqualsAndRestaurantId(dishId, restaurantId)) > 0;
    }

    @Override
    public boolean existsByRestaurantIdAndOwnerId(Long restaurantId, Long currentUserId) {
        return dishRepository.count(
                DishSpecifications.restaurantIdEqualsAndOwnerId(restaurantId, currentUserId)) > 0;
    }
}
