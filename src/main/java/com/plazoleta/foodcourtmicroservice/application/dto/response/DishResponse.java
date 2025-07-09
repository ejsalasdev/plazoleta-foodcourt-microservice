package com.plazoleta.foodcourtmicroservice.application.dto.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DishResponse", description = "Response body for dish data.")
public record DishResponse(
        @Schema(description = "Dish ID", example = "1") Long id,
        @Schema(description = "Dish name", example = "Classic Burger") String name,
        @Schema(description = "Dish price", example = "15000.00") BigDecimal price,
        @Schema(description = "Dish description", example = "Juicy beef burger with cheddar cheese and fresh vegetables.") String description,
        @Schema(description = "Image URL", example = "https://cdn.example.com/img/burger.jpg") String urlImage,
        @Schema(description = "Category name", example = "Burgers") String categoryName,
        @Schema(description = "Restaurant name", example = "Burger House") String restaurantName) {

}