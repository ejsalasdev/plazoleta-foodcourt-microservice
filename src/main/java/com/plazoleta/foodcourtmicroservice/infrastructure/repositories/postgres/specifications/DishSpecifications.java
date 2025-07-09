package com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.specifications;

import com.plazoleta.foodcourtmicroservice.infrastructure.entities.DishEntity;
import com.plazoleta.foodcourtmicroservice.infrastructure.utils.constants.SpecificationConstants;
import org.springframework.data.jpa.domain.Specification;

public class DishSpecifications {

    private DishSpecifications() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<DishEntity> nameEqualsIgnoreCaseAndRestaurantId(String name, Long restaurantId) {
        return (root, query, cb) -> cb.and(
                cb.equal(cb.lower(root.get(SpecificationConstants.NAME_FIELD)), name.toLowerCase()),
                cb.equal(root.get(SpecificationConstants.RESTAURANT_FIELD).get(SpecificationConstants.ID_FIELD), restaurantId));
    }

    public static Specification<DishEntity> idEqualsAndRestaurantId(Long dishId, Long restaurantId) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get(SpecificationConstants.ID_FIELD), dishId),
                cb.equal(root.get(SpecificationConstants.RESTAURANT_FIELD).get(SpecificationConstants.ID_FIELD), restaurantId));
    }

    public static Specification<DishEntity> restaurantIdEqualsAndOwnerId(Long restaurantId, Long currentUserId) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get(SpecificationConstants.RESTAURANT_FIELD).get(SpecificationConstants.ID_FIELD), restaurantId),
                cb.equal(root.get(SpecificationConstants.RESTAURANT_FIELD).get(SpecificationConstants.OWNER_ID_FIELD), currentUserId));
    }

    public static Specification<DishEntity> restaurantIdEquals(Long restaurantId) {
        return (root, query, cb) -> cb.equal(root.get(SpecificationConstants.RESTAURANT_FIELD).get(SpecificationConstants.ID_FIELD), restaurantId);
    }

    public static Specification<DishEntity> categoryIdEquals(Long categoryId) {
        return (root, query, cb) -> cb.equal(root.get(SpecificationConstants.CATEGORY_FIELD).get(SpecificationConstants.ID_FIELD), categoryId);
    }
}
