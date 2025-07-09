package com.plazoleta.foodcourtmicroservice.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveDishRequest;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.model.CategoryModel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DishRequestMapper {

    @Mapping(target = "restaurant", source = "restaurantId", qualifiedByName = "restaurantIdToRestaurantModel")
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "categoryIdToCategoryModel")
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

    @Named("categoryIdToCategoryModel")
    default CategoryModel categoryIdToCategoryModel(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        CategoryModel category = new CategoryModel();
        category.setId(categoryId);
        return category;

    }
}
