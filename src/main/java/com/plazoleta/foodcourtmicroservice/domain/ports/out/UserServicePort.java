package com.plazoleta.foodcourtmicroservice.domain.ports.out;

public interface UserServicePort {
    
    String getUserRoleById(Long userId);
    
    Long getUserRestaurantId(Long userId);
    
    String getUserPhoneNumber(Long userId);
}
