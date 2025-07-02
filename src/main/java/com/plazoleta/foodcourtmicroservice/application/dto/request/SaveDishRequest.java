
package com.plazoleta.foodcourtmicroservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SaveDishRequest", description = "Request body for creating a new dish.")
public record SaveDishRequest(
    @Schema(description = "Dish name", example = "Hamburguesa Clásica")
    String name,

    @Schema(description = "Dish price (integer, greater than 0)", example = "15000")
    Integer price,

    @Schema(description = "Dish description", example = "Jugosa hamburguesa de res con queso cheddar y vegetales frescos.")
    String description,

    @Schema(description = "Image URL (http/https, ends with jpg/png/jpeg/gif/bmp)", example = "https://cdn.ejemplo.com/img/hamburguesa.jpg")
    String urlImage,

    @Schema(description = "Category ID", example = "1")
    Long categoryId,

    @Schema(description = "Restaurant ID", example = "10")
    Long restaurantId
) {}
