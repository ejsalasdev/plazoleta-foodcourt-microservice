package com.plazoleta.foodcourtmicroservice.application.client.handler;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.plazoleta.foodcourtmicroservice.application.client.dto.OrderTrackingRequest;

@FeignClient(name = "tracking-microservice", url = "${tracking-microservice.url}")
public interface OrderTrackingHandlerClient {

    @PostMapping("/api/v1/tracking/internal/track")
    void trackOrder(@RequestBody OrderTrackingRequest request);

}
