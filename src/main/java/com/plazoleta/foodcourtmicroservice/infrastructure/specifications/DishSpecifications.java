package com.plazoleta.foodcourtmicroservice.infrastructure.specifications;

import com.plazoleta.foodcourtmicroservice.infrastructure.entities.DishEntity;
import org.springframework.data.jpa.domain.Specification;

public class DishSpecifications {

    private DishSpecifications() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<DishEntity> nameEqualsIgnoreCaseAndRestaurantId(String name, Long restaurantId) {
        return (root, query, cb) -> cb.and(
                cb.equal(cb.lower(root.get("name")), name.toLowerCase()),
                cb.equal(root.get("restaurant").get("id"), restaurantId));
    }

    public static Specification<DishEntity> idEqualsAndRestaurantId(Long dishId, Long restaurantId) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("id"), dishId),
                cb.equal(root.get("restaurant").get("id"), restaurantId));
    }
}
