package com.plazoleta.foodcourtmicroservice.application.mappers;

import com.plazoleta.foodcourtmicroservice.application.dto.response.OrderDishResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.OrderResponse;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderDishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {RestaurantResponseMapper.class, DishResponseMapper.class})
public interface OrderResponseMapper {

    OrderResponse modelToResponse(OrderModel orderModel);

    OrderDishResponse modelToResponse(OrderDishModel orderDishModel);

    List<OrderResponse> orderModelListToResponseList(List<OrderModel> orderModels);

    List<OrderDishResponse> orderDishModelListToResponseList(List<OrderDishModel> orderDishModels);
}
