package com.plazoleta.foodcourtmicroservice.domain.ports.out;

public interface NotificationServicePort {
    void sendOrderReadyNotification(Long orderId, String phoneNumber, String securityPin);
}
