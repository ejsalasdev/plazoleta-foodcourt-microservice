package com.plazoleta.foodcourtmicroservice.infrastructure.mappers;

import org.mapstruct.Mapper;

import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.infrastructure.entities.RestaurantEntity;

@Mapper(componentModel = "spring")
public interface RestaurantEntityMapper {

    RestaurantEntity modelToEntity(RestaurantModel restaurantModel);
    RestaurantModel entityToModel(RestaurantEntity restaurantEntity);

}
