package com.plazoleta.foodcourtmicroservice.application.handler.impl;

import com.plazoleta.foodcourtmicroservice.application.dto.request.CreateOrderRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.OrderResponse;
import com.plazoleta.foodcourtmicroservice.application.handler.OrderHandler;
import com.plazoleta.foodcourtmicroservice.application.mappers.OrderRequestMapper;
import com.plazoleta.foodcourtmicroservice.application.mappers.OrderResponseMapper;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.OrderServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderHandlerImpl implements OrderHandler {

    private final OrderRequestMapper orderRequestMapper;
    private final OrderServicePort orderServicePort;
    private final OrderResponseMapper orderResponseMapper;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        OrderModel orderModel = orderRequestMapper.requestToModel(request);
        OrderModel createdOrder = orderServicePort.createOrder(orderModel);
        
        return orderResponseMapper.modelToResponse(createdOrder);
    }
}
