
package com.plazoleta.foodcourtmicroservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SaveRestaurantRequest", description = "Required data to create a restaurant")
public record SaveRestaurantRequest(
        @Schema(description = "Restaurant name", example = "La Pizzeria", minLength = 2, maxLength = 50) String name,

        @Schema(description = "Restaurant NIT (unique)", example = "123456789", minLength = 5, maxLength = 20) String nit,

        @Schema(description = "Restaurant address", example = "Calle 123 #45-67", minLength = 5, maxLength = 100) String address,

        @Schema(description = "Contact phone number", example = "+573001234567", minLength = 7, maxLength = 15) String phoneNumber,

        @Schema(description = "Restaurant logo URL", example = "https://miapp.com/logos/pizzeria.png", maxLength = 255) String urlLogo,

        @Schema(description = "Owner user ID", example = "2") Long ownerId) {
}
