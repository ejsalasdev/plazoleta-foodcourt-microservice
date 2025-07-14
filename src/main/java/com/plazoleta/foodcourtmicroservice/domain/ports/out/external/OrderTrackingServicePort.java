package com.plazoleta.foodcourtmicroservice.domain.ports.out.external;

import com.plazoleta.foodcourtmicroservice.application.client.dto.OrderTrackingRequest;

public interface OrderTrackingServicePort {

    void trackOrder(OrderTrackingRequest request);
}
