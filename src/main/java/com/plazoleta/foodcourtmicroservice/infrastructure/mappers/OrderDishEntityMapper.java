package com.plazoleta.foodcourtmicroservice.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.plazoleta.foodcourtmicroservice.domain.model.OrderDishModel;
import com.plazoleta.foodcourtmicroservice.infrastructure.entities.OrderDishEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { DishEntityMapper.class })
public interface OrderDishEntityMapper {

    @Mapping(target = "order", ignore = true)
    OrderDishModel entityToModel(OrderDishEntity orderDishEntity);

    @Mapping(target = "order", ignore = true)
    OrderDishEntity modelToEntity(OrderDishModel orderDishModel);

    List<OrderDishModel> entityListToModelList(List<OrderDishEntity> orderDishEntities);

    List<OrderDishEntity> modelListToEntityList(List<OrderDishModel> orderDishModels);
}
