package com.plazoleta.foodcourtmicroservice.domain.ports.in;

import com.plazoleta.foodcourtmicroservice.domain.model.OrderModel;

public interface OrderServicePort {
    OrderModel createOrder(OrderModel orderModel);
}
