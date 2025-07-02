package com.plazoleta.foodcourtmicroservice.application.mappers;

import org.mapstruct.Mapper;

import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveRestaurantRequest;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface RestaurantRequestMapper {

    RestaurantModel requestToModel(SaveRestaurantRequest request);

}
