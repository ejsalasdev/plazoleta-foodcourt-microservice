package com.plazoleta.foodcourtmicroservice.application.mappers;

import org.mapstruct.Mapper;
import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveDishRequest;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DishRequestMapper {
    
    DishModel requestToModel(SaveDishRequest request);
}
