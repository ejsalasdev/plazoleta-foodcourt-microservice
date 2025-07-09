package com.plazoleta.foodcourtmicroservice.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.plazoleta.foodcourtmicroservice.application.dto.response.RestaurantResponse;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantResponseMapper {

    RestaurantResponse modelToResponse(RestaurantModel restaurantModel);
}
