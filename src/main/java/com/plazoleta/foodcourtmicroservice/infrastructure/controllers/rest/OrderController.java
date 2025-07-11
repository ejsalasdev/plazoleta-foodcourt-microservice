package com.plazoleta.foodcourtmicroservice.infrastructure.controllers.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plazoleta.foodcourtmicroservice.application.dto.request.CreateOrderRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.request.DeliverOrderRequest;
import com.plazoleta.foodcourtmicroservice.application.dto.response.AssignOrderResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.DeliverOrderResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.OrderReadyResponse;
import com.plazoleta.foodcourtmicroservice.application.dto.response.OrderResponse;
import com.plazoleta.foodcourtmicroservice.application.handler.OrderHandler;
import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;
import com.plazoleta.foodcourtmicroservice.infrastructure.exceptionhandler.ExceptionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
                        @ApiResponse(responseCode = "400", description = "Invalid request data or dish validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden - User does not have CUSTOMER role", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Restaurant or dish not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "409", description = "Customer already has an active order", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
        })
        public ResponseEntity<OrderResponse> createOrder(
                        @Parameter(description = "Order creation request containing restaurant ID and list of dishes with quantities", required = true) @Valid @RequestBody CreateOrderRequest request) {
                OrderResponse response = orderHandler.createOrder(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping
        @Operation(summary = "Get orders by status", description = "Retrieves a paginated list of orders filtered by status for the authenticated employee's restaurant")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageInfo.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden - User does not have EMPLOYEE role", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Employee not associated with any restaurant", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
        })
        public ResponseEntity<PageInfo<OrderResponse>> getOrdersByStatus(
                        @Parameter(description = "Order status to filter by", required = true, example = "PENDING") @RequestParam OrderStatusEnum status,
                        @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") Integer page,
                        @Parameter(description = "Number of elements per page", example = "10") @RequestParam(defaultValue = "10") Integer size) {
                PageInfo<OrderResponse> response = orderHandler.getOrdersByStatus(status, page, size);
                return ResponseEntity.ok(response);
        }

        @PatchMapping("/{orderId}/assign")
        @Operation(summary = "Assign order to employee and change status to IN_PREPARATION", description = "Allows an employee to assign themselves to a PENDING order and change its status to IN_PREPARATION")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Order assigned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssignOrderResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid order ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden - User does not have EMPLOYEE role", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Order not found or employee not associated with restaurant", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "409", description = "Order is not in PENDING status or doesn't belong to employee's restaurant", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
        })
        public ResponseEntity<AssignOrderResponse> assignOrderToEmployee(
                        @Parameter(description = "ID of the order to assign", example = "1") @PathVariable Long orderId) {
                AssignOrderResponse response = orderHandler.assignOrderToEmployee(orderId);
                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @PatchMapping("/{orderId}/ready")
        @Operation(summary = "Mark order as ready", description = "Allows an employee to mark an IN_PREPARATION order as READY and send SMS notification to customer")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Order marked as ready successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderReadyResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid order ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden - User does not have EMPLOYEE role", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Order not found or employee not associated with restaurant", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "409", description = "Order is not in IN_PREPARATION status or doesn't belong to employee's restaurant", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
        })
        public ResponseEntity<OrderReadyResponse> markOrderAsReady(
                        @Parameter(description = "ID of the order to mark as ready", example = "1") @PathVariable Long orderId) {
                OrderReadyResponse response = orderHandler.markOrderAsReady(orderId);
                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @PatchMapping("/{orderId}/deliver")
        @Operation(summary = "Deliver an order", description = "Mark an order as delivered by validating the security PIN")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Order delivered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeliverOrderResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request or security PIN format", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "403", description = "User not authorized (only employees can deliver orders)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Order not found or employee not associated with restaurant", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "409", description = "Order is not in READY status, doesn't belong to employee's restaurant, or invalid security PIN", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
        })
        public ResponseEntity<DeliverOrderResponse> deliverOrder(
                        @Parameter(description = "ID of the order to deliver", example = "1") @PathVariable Long orderId,
                        @Parameter(description = "Delivery request with security PIN") @Valid @RequestBody DeliverOrderRequest request) {
                DeliverOrderResponse response = orderHandler.deliverOrder(orderId, request);
                return ResponseEntity.status(HttpStatus.OK).body(response);
        }
}
