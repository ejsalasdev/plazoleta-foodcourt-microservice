package com.plazoleta.foodcourtmicroservice.infrastructure.mappers;

import org.mapstruct.Mapper;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.infrastructure.entities.DishEntity;

@Mapper(componentModel = "spring")
public interface DishEntityMapper {

    DishEntity modelToEntity(DishModel dishModel);

    DishModel entityToModel(DishEntity dishEntity);
}
