package com.plazoleta.foodcourtmicroservice.infrastructure.mappers;

import org.mapstruct.Mapper;

import com.plazoleta.foodcourtmicroservice.domain.model.CategoryModel;
import com.plazoleta.foodcourtmicroservice.infrastructure.entities.CategoryEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface CategoryEntityMapper {

    CategoryEntity modelToEntity(CategoryModel model);

    CategoryModel entityToModel(CategoryEntity entity);
}
