package com.plazoleta.foodcourtmicroservice.infrastructure.controllers.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plazoleta.foodcourtmicroservice.application.dto.request.CreateOrderRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.OrderResponse;
import com.plazoleta.foodcourtmicroservice.application.handler.OrderHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Operations related to order management")
public class OrderController {

        private final OrderHandler orderHandler;

        @PostMapping
        @Operation(summary = "Create a new order", description = "Creates a new order for the authenticated customer")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Order created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden - User does not have CUSTOMER role", content = @Content),
                        @ApiResponse(responseCode = "409", description = "Customer already has an active order", content = @Content)
        })
        public ResponseEntity<OrderResponse> createOrder(
                        @Parameter(description = "Order creation request", required = true) @RequestBody CreateOrderRequest request) {

                OrderResponse response = orderHandler.createOrder(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
}
