package com.plazoleta.foodcourtmicroservice.infrastructure.mappers;

import java.util.List;
import java.util.ArrayList;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.plazoleta.foodcourtmicroservice.domain.model.OrderModel;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderDishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.infrastructure.entities.OrderEntity;
import com.plazoleta.foodcourtmicroservice.infrastructure.entities.OrderDishEntity;
import com.plazoleta.foodcourtmicroservice.infrastructure.entities.DishEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { RestaurantEntityMapper.class,
        DishEntityMapper.class })
public interface OrderEntityMapper {

    @Mapping(target = "orderDishes", ignore = true)
    OrderModel entityToModel(OrderEntity orderEntity);

    @Mapping(target = "orderDishes", ignore = true)
    OrderEntity modelToEntity(OrderModel orderModel);

    default OrderModel entityToModelWithDishes(OrderEntity orderEntity) {
        if (orderEntity == null) {
            return null;
        }
        
        OrderModel orderModel = entityToModel(orderEntity);
        
        if (orderEntity.getOrderDishes() != null) {
            List<OrderDishModel> orderDishModels = new ArrayList<>();
            for (OrderDishEntity orderDishEntity : orderEntity.getOrderDishes()) {
                OrderDishModel orderDishModel = orderDishEntityToModel(orderDishEntity);
                orderDishModel.setOrder(orderModel);
                orderDishModels.add(orderDishModel);
            }
            orderModel.setOrderDishes(orderDishModels);
        }
        
        return orderModel;
    }

    default OrderEntity modelToEntityWithDishes(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        
        OrderEntity orderEntity = modelToEntity(orderModel);
        
        if (orderModel.getOrderDishes() != null) {
            List<OrderDishEntity> orderDishEntities = new ArrayList<>();
            for (OrderDishModel orderDishModel : orderModel.getOrderDishes()) {
                OrderDishEntity orderDishEntity = orderDishModelToEntity(orderDishModel);
                orderDishEntity.setOrder(orderEntity);
                orderDishEntities.add(orderDishEntity);
            }
            orderEntity.setOrderDishes(orderDishEntities);
        }
        
        return orderEntity;
    }

    default OrderDishModel orderDishEntityToModel(OrderDishEntity orderDishEntity) {
        if (orderDishEntity == null) {
            return null;
        }
        
        OrderDishModel orderDishModel = new OrderDishModel();
        orderDishModel.setId(orderDishEntity.getId());
        orderDishModel.setQuantity(orderDishEntity.getQuantity());
        
        if (orderDishEntity.getDish() != null) {
            orderDishModel.setDish(mapDishEntityToModel(orderDishEntity.getDish()));
        }
        
        return orderDishModel;
    }

    default OrderDishEntity orderDishModelToEntity(OrderDishModel orderDishModel) {
        if (orderDishModel == null) {
            return null;
        }
        
        OrderDishEntity orderDishEntity = new OrderDishEntity();
        orderDishEntity.setId(orderDishModel.getId());
        orderDishEntity.setQuantity(orderDishModel.getQuantity());
        
        if (orderDishModel.getDish() != null) {
            orderDishEntity.setDish(mapDishModelToEntity(orderDishModel.getDish()));
        }
        
        return orderDishEntity;
    }

    @Mapping(target = "restaurant", ignore = true)
    DishModel mapDishEntityToModel(DishEntity dishEntity);
    
    @Mapping(target = "restaurant", ignore = true)
    DishEntity mapDishModelToEntity(DishModel dishModel);
}
