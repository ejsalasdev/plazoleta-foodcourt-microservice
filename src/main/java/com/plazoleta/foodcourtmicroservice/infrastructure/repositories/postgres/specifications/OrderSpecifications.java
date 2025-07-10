package com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.specifications;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;
import com.plazoleta.foodcourtmicroservice.infrastructure.entities.OrderEntity;
import com.plazoleta.foodcourtmicroservice.infrastructure.utils.constants.SpecificationConstants;

public class OrderSpecifications {

    private OrderSpecifications() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<OrderEntity> customerIdEquals(Long customerId) {
        return (root, query, cb) -> cb.equal(root.get(SpecificationConstants.CUSTOMER_ID_FIELD), customerId);
    }

    public static Specification<OrderEntity> restaurantIdEquals(Long restaurantId) {
        return (root, query, cb) -> cb.equal(root.get(SpecificationConstants.RESTAURANT_FIELD).get(SpecificationConstants.ID_FIELD), restaurantId);
    }

    public static Specification<OrderEntity> statusIn(List<OrderStatusEnum> statuses) {
        return (root, query, cb) -> root.get(SpecificationConstants.STATUS_FIELD).in(statuses);
    }

    public static Specification<OrderEntity> statusEquals(OrderStatusEnum status) {
        return (root, query, cb) -> cb.equal(root.get(SpecificationConstants.STATUS_FIELD), status);
    }
}
