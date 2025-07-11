package com.plazoleta.foodcourtmicroservice.infrastructure.adapters.external;

import org.springframework.http.ResponseEntity;

import com.plazoleta.foodcourtmicroservice.application.client.dto.NotificationResponse;
import com.plazoleta.foodcourtmicroservice.application.client.dto.SendSmsRequest;
import com.plazoleta.foodcourtmicroservice.application.client.handler.MessagingHandlerClient;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.NotificationServicePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class NotificationServiceAdapter implements NotificationServicePort {

    private final MessagingHandlerClient messagingServiceClient;

    @Override
    public void sendOrderReadyNotification(Long orderId, String phoneNumber, String securityPin) {
        try {
            String message = String.format("Your order is ready! Use PIN %s to pick it up.", securityPin);

            SendSmsRequest request = new SendSmsRequest(phoneNumber, message, securityPin, orderId);

            ResponseEntity<NotificationResponse> response = messagingServiceClient.sendSmsNotification(request);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("📱 SMS sent successfully - Order: {}, Phone: {}, PIN: {}", orderId, phoneNumber, securityPin);
            } else {
                log.error("❌ Failed to send SMS - Order: {}, Phone: {}, Status: {}", orderId, phoneNumber, response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("❌ Error sending SMS notification - Order: {}, Phone: {}, Error: {}", orderId, phoneNumber, e.getMessage());
        }
    }
}
