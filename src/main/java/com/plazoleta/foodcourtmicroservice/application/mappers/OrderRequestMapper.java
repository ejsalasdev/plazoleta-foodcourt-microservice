package com.plazoleta.foodcourtmicroservice.application.mappers;

import com.plazoleta.foodcourtmicroservice.application.dto.request.CreateOrderRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.request.OrderDishRequest;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderDishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderModel;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "restaurant", source = "restaurantId", qualifiedByName = "restaurantIdToRestaurantModel")
    @Mapping(target = "orderDishes", source = "dishes")
    OrderModel requestToModel(CreateOrderRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "dish", source = "dishId", qualifiedByName = "dishIdToDishModel")
    OrderDishModel requestToModel(OrderDishRequest request);

    List<OrderDishModel> requestListToModelList(List<OrderDishRequest> requests);

    @Named("restaurantIdToRestaurantModel")
    default RestaurantModel restaurantIdToRestaurantModel(Long restaurantId) {
        if (restaurantId == null) {
            return null;
        }
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);
        return restaurant;
    }

    @Named("dishIdToDishModel")
    default DishModel dishIdToDishModel(Long dishId) {
        if (dishId == null) {
            return null;
        }
        DishModel dish = new DishModel();
        dish.setId(dishId);
        return dish;
    }
}
