package com.plazoleta.foodcourtmicroservice.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.plazoleta.foodcourtmicroservice.application.dto.response.DishResponse;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DishResponseMapper {

    @Mapping(source = "restaurant.name", target = "restaurantName")
    DishResponse modelToResponse(DishModel dishModel);
}
