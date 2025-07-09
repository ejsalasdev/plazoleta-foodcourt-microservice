package com.plazoleta.foodcourtmicroservice.application.mappers;

import org.mapstruct.Mapper;
import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveDishRequest;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;

import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DishRequestMapper {

    @Mapping(target = "restaurant", source = "restaurantId", qualifiedByName = "restaurantIdToRestaurantModel")
    DishModel requestToModel(SaveDishRequest request);

    @Named("restaurantIdToRestaurantModel")
    default RestaurantModel restaurantIdToRestaurantModel(Long restaurantId) {
        if (restaurantId == null) {
            return null;
        }
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);
        return restaurant;
    }
}
