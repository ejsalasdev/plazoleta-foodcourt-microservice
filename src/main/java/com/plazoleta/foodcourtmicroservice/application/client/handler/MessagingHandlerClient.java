package com.plazoleta.foodcourtmicroservice.application.client.handler;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.plazoleta.foodcourtmicroservice.application.client.dto.NotificationResponse;
import com.plazoleta.foodcourtmicroservice.application.client.dto.SendSmsRequest;

@FeignClient(name = "messaging-microservice", url = "${messaging-microservice.url}")
public interface MessagingHandlerClient {

    @PostMapping("/api/v1/notifications/sms")
    ResponseEntity<NotificationResponse> sendSmsNotification(@RequestBody SendSmsRequest request);
}
