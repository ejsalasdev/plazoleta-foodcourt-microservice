package com.plazoleta.foodcourtmicroservice.infrastructure.adapters.external;

import com.plazoleta.foodcourtmicroservice.domain.ports.out.NotificationServicePort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationServiceAdapter implements NotificationServicePort {

    @Override
    public void sendOrderReadyNotification(String phoneNumber, String securityPin) {
        // TODO: Implement real SMS notification via messaging microservice
        log.info("📱 SMS Notification - Phone: {}, PIN: {}, Message: 'Your order is ready! Use PIN {} to pick it up.'", 
                phoneNumber, securityPin, securityPin);
    }
}
