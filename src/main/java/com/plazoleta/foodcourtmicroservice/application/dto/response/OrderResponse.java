package com.plazoleta.foodcourtmicroservice.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;

public record OrderResponse(
        Long id,
        Long customerId,
        LocalDateTime date,
        OrderStatusEnum status,
        Long employeeId,
        RestaurantResponse restaurant,
        List<OrderDishResponse> orderDishes) {
}
