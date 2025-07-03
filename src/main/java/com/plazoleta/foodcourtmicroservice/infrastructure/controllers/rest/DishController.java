package com.plazoleta.foodcourtmicroservice.infrastructure.controllers.rest;

// ...existing code...

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveDishRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.request.UpdateDishRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.SaveDishResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.UpdateDishResponse;
import com.plazoleta.foodcourtmicroservice.application.handler.DishHandler;
// ...existing code...
import com.plazoleta.foodcourtmicroservice.infrastructure.exceptionhandler.ExceptionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/dish")
@RequiredArgsConstructor
@Tag(name = "Dish", description = "Operations related to dishes")
public class DishController {

        private final DishHandler dishHandler;

        @Operation(summary = "Create a new dish", description = "Creates a new dish with the provided data.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Dish created successfully", content = @Content(schema = @Schema(implementation = SaveDishResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "409", description = "Dish with given name already exists in this restaurant", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        })
        @PostMapping
        public ResponseEntity<SaveDishResponse> save(@RequestBody SaveDishRequest request) {
                return ResponseEntity.status(HttpStatus.CREATED).body(dishHandler.save(request));
        }

        @PatchMapping("/{dishId}")
        public ResponseEntity<UpdateDishResponse> update(@PathVariable Long dishId, @RequestBody UpdateDishRequest request) {
                return ResponseEntity.status(HttpStatus.OK).body(dishHandler.update(dishId, request));
        }
}
