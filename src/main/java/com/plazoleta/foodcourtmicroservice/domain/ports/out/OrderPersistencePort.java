package com.plazoleta.foodcourtmicroservice.domain.ports.out;

import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderModel;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;

import java.util.List;
import java.util.Optional;

public interface OrderPersistencePort {
    OrderModel saveOrder(OrderModel orderModel);

    Optional<OrderModel> findOrderById(Long orderId);

    List<OrderModel> findOrdersByCustomerId(Long customerId);

    List<OrderModel> findOrdersByRestaurantId(Long restaurantId);

    boolean hasActiveOrdersForCustomer(Long customerId);

    PageInfo<OrderModel> findOrdersByRestaurantIdAndStatus(Long restaurantId, OrderStatusEnum status, Integer page,
            Integer size);
    
    OrderModel updateOrder(OrderModel orderModel);
}
