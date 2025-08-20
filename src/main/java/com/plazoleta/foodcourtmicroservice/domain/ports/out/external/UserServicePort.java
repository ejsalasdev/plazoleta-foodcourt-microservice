package com.plazoleta.foodcourtmicroservice.domain.ports.out.external;

public interface UserServicePort {
    
    String getUserRoleById(Long userId);
    
    Long getUserRestaurantId(Long userId);
    
    String getUserPhoneNumber(Long userId);
    
    String getUserEmail(Long userId);
}
