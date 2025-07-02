package com.plazoleta.foodcourtmicroservice.infrastructure.controllers.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plazoleta.foodcourtmicroservice.application.dto.request.SaveDishRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.SaveDishResponse;
import com.plazoleta.foodcourtmicroservice.application.handler.DishHandler;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/dish")
@RequiredArgsConstructor
@Tag(name = "Dish", description = "Operations related to dishes")
public class DishController {

    private final DishHandler dishHandler;

    @PostMapping
    public ResponseEntity<SaveDishResponse> save(@RequestBody SaveDishRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dishHandler.save(request));
    }
}
