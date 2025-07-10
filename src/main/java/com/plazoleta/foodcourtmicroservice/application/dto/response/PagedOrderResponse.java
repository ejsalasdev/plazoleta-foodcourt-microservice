package com.plazoleta.foodcourtmicroservice.application.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PagedOrderResponse", description = "Paginated response containing orders and pagination information.")
public record PagedOrderResponse(
    @Schema(description = "List of orders", example = "[]") List<OrderResponse> content,
    
    @Schema(description = "Total number of elements", example = "25") long totalElements,
    
    @Schema(description = "Total number of pages", example = "3") int totalPages,
    
    @Schema(description = "Current page number", example = "0") int currentPage,
    
    @Schema(description = "Number of elements per page", example = "10") int pageSize,
    
    @Schema(description = "Whether there is a next page", example = "true") boolean hasNext,
    
    @Schema(description = "Whether there is a previous page", example = "false") boolean hasPrevious
) {
}
