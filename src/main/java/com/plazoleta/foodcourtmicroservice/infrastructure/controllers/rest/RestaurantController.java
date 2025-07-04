package com.plazoleta.foodcourtmicroservice.infrastructure.controllers.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveRestaurantRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.SaveRestaurantResponse;
import com.plazoleta.foodcourtmicroservice.application.handler.RestaurantHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "Operations related to restaurants")
public class RestaurantController {

    private final RestaurantHandler restaurantHandler;

    @Operation(summary = "Create a new restaurant", description = "Creates a new restaurant with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant created successfully", content = @Content(schema = @Schema(implementation = SaveRestaurantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = com.plazoleta.foodcourtmicroservice.infrastructure.exceptionhandler.ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Restaurant with given NIT already exists", content = @Content(schema = @Schema(implementation = com.plazoleta.foodcourtmicroservice.infrastructure.exceptionhandler.ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = com.plazoleta.foodcourtmicroservice.infrastructure.exceptionhandler.ExceptionResponse.class)))
    })
    @PostMapping("/")
    public ResponseEntity<SaveRestaurantResponse> save(@RequestBody SaveRestaurantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantHandler.save(request));
    }
}
