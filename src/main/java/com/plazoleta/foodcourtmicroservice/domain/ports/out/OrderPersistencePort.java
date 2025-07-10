package com.plazoleta.foodcourtmicroservice.domain.ports.out;

import com.plazoleta.foodcourtmicroservice.domain.model.OrderModel;

import java.util.List;
import java.util.Optional;

public interface OrderPersistencePort {
    OrderModel saveOrder(OrderModel orderModel);
    Optional<OrderModel> findOrderById(Long orderId);
    List<OrderModel> findOrdersByCustomerId(Long customerId);
    List<OrderModel> findOrdersByRestaurantId(Long restaurantId);
    boolean hasActiveOrdersForCustomer(Long customerId);
}
