package com.plazoleta.foodcourtmicroservice.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.infrastructure.entities.DishEntity;

@Mapper(componentModel = "spring", uses = {RestaurantEntityMapper.class, CategoryEntityMapper.class})
public interface DishEntityMapper {

    @Mapping(source = "restaurant", target = "restaurant")
    @Mapping(source = "category", target = "category")
    DishEntity modelToEntity(DishModel dishModel);

    @Mapping(source = "restaurant", target = "restaurant")
    @Mapping(source = "category", target = "category")
    DishModel entityToModel(DishEntity dishEntity);
}
