package com.plazoleta.foodcourtmicroservice.domain.ports.in;

import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderModel;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;

public interface OrderServicePort {
    OrderModel createOrder(OrderModel orderModel);
    PageInfo<OrderModel> getOrdersByRestaurantAndStatus(OrderStatusEnum status, Integer page, Integer size);
    OrderModel assignOrderToEmployeeAndChangeStatus(Long orderId);
    OrderModel markOrderAsReady(Long orderId);
    OrderModel deliverOrder(Long orderId, String securityPin);
    OrderModel cancelOrder(Long orderId);
}
