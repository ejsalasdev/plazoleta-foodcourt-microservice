package com.plazoleta.foodcourtmicroservice.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.infrastructure.entities.DishEntity;

@Mapper(componentModel = "spring", uses = RestaurantEntityMapper.class)
public interface DishEntityMapper {

    @Mapping(source = "restaurant", target = "restaurant")
    DishEntity modelToEntity(DishModel dishModel);

    @Mapping(source = "restaurant", target = "restaurant")
    DishModel entityToModel(DishEntity dishEntity);
}
