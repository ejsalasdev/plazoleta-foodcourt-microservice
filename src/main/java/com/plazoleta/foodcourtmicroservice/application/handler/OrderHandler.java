package com.plazoleta.foodcourtmicroservice.application.handler;

import com.plazoleta.foodcourtmicroservice.application.dto.request.CreateOrderRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.OrderResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.PagedOrderResponse;
import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;

public interface OrderHandler {
    OrderResponse createOrder(CreateOrderRequest request);
    
    PagedOrderResponse getOrdersByStatus(OrderStatusEnum status, Integer page, Integer size);
}
