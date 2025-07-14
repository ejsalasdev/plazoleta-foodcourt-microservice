package com.plazoleta.foodcourtmicroservice.infrastructure.adapters.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.plazoleta.foodcourtmicroservice.application.client.dto.OrderTrackingRequest;
import com.plazoleta.foodcourtmicroservice.application.client.handler.OrderTrackingHandlerClient;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.external.OrderTrackingServicePort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderTrackingServiceAdapter implements OrderTrackingServicePort {

    private static final Logger logger = LoggerFactory.getLogger(OrderTrackingServiceAdapter.class);
    private final OrderTrackingHandlerClient orderTrackingHandlerClient;

    @Override
    public void trackOrder(OrderTrackingRequest request) {
        try {
            orderTrackingHandlerClient.trackOrder(request);
        } catch (Exception e) {
            logger.warn("Failed to track order {}: {}", request.orderId(), e.getMessage());
        }
    }

}
