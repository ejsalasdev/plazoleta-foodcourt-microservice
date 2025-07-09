
package com.plazoleta.foodcourtmicroservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SaveDishRequest", description = "Request body for creating a new dish.")
public record SaveDishRequest(
        @Schema(description = "Dish name", example = "Classic Burger") String name,

        @Schema(description = "Dish price (integer, greater than 0)", example = "15000") Integer price,

        @Schema(description = "Dish description", example = "Juicy beef burger with cheddar cheese and fresh vegetables.") String description,

        @Schema(description = "Image URL (http or https, ends with jpg/png/jpeg/gif/bmp)", example = "https://cdn.example.com/img/burger.jpg") String urlImage,

        @Schema(description = "Category ID", example = "2") Long categoryId,

        @Schema(description = "Restaurant ID", example = "10") Long restaurantId) {
}
