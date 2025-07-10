package com.plazoleta.foodcourtmicroservice.application.handler.impl;

import com.plazoleta.foodcourtmicroservice.application.dto.request.CreateOrderRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.OrderResponse;
import com.plazoleta.foodcourtmicroservice.application.handler.OrderHandler;
import com.plazoleta.foodcourtmicroservice.application.mappers.OrderRequestMapper;
import com.plazoleta.foodcourtmicroservice.application.mappers.OrderResponseMapper;
import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.OrderServicePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    
    @Override
    public PageInfo<OrderResponse> getOrdersByStatus(OrderStatusEnum status, Integer page, Integer size) {
        PageInfo<OrderModel> orderPage = orderServicePort.getOrdersByRestaurantAndStatus(status, page, size);
        
        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(orderResponseMapper::modelToResponse)
                .toList();
        
        return new PageInfo<>(
                orderResponses,
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.getCurrentPage(),
                orderPage.getPageSize(),
                orderPage.isHasNext(),
                orderPage.isHasPrevious()
        );
    }
}
