package com.plazoleta.foodcourtmicroservice.application.handler;

import com.plazoleta.foodcourtmicroservice.application.dto.request.CreateOrderRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.request.DeliverOrderRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.OrderResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.AssignOrderResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.OrderReadyResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.DeliverOrderResponse;
import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;

public interface OrderHandler {
    OrderResponse createOrder(CreateOrderRequest request);
    
    PageInfo<OrderResponse> getOrdersByStatus(OrderStatusEnum status, Integer page, Integer size);
    
    AssignOrderResponse assignOrderToEmployee(Long orderId);
    
    OrderReadyResponse markOrderAsReady(Long orderId);
    
    DeliverOrderResponse deliverOrder(Long orderId, DeliverOrderRequest request);
}
