package com.plazoleta.foodcourtmicroservice.domain.utils;

import java.security.SecureRandom;

public class SecurityPinGenerator {
    
    private static final String NUMBERS = "0123456789";
    private static final int PIN_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();
    
    private SecurityPinGenerator() {
        // Utility class - private constructor
    }
    
    public static String generateSecurityPin() {
        StringBuilder pin = new StringBuilder(PIN_LENGTH);
        
        for (int i = 0; i < PIN_LENGTH; i++) {
            int index = random.nextInt(NUMBERS.length());
            pin.append(NUMBERS.charAt(index));
        }
        
        return pin.toString();
    }
}
