package com.plazoleta.foodcourtmicroservice.domain.ports.out.external;

public interface NotificationServicePort {
    void sendOrderReadyNotification(Long orderId, String phoneNumber, String securityPin);
    void sendOrderCancelledNotification(Long orderId, String phoneNumber);
}
