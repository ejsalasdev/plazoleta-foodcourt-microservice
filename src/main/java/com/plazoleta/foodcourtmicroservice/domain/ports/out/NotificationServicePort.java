package com.plazoleta.foodcourtmicroservice.domain.ports.out;

public interface NotificationServicePort {
    void sendOrderReadyNotification(String phoneNumber, String securityPin);
}
