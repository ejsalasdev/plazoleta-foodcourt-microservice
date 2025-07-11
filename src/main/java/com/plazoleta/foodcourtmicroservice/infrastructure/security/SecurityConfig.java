package com.plazoleta.foodcourtmicroservice.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.plazoleta.foodcourtmicroservice.infrastructure.security.jwt.JwtTokenValidator;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenValidator jwtTokenValidator;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http.requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs*/**").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/v1/restaurant/").hasAuthority("ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/api/v1/restaurant/").hasAuthority("CUSTOMER");
                    http.requestMatchers(HttpMethod.GET, "/api/v1/restaurant/owner/{ownerId}").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/v1/dish/").hasAuthority("OWNER");
                    http.requestMatchers(HttpMethod.PATCH, "/api/v1/dish/{id}").hasAuthority("OWNER");
                    http.requestMatchers(HttpMethod.PATCH, "/api/v1/dish/{id}/active").hasAuthority("OWNER");
                    http.requestMatchers(HttpMethod.GET, "/api/v1/dish/restaurant/{restaurantId}").hasAuthority("CUSTOMER");
                    http.requestMatchers(HttpMethod.POST, "/api/v1/orders").hasAuthority("CUSTOMER");
                    http.requestMatchers(HttpMethod.GET, "/api/v1/orders").hasAuthority("EMPLOYEE");
                    http.requestMatchers(HttpMethod.PATCH, "/api/v1/orders/{orderId}/assign").hasAuthority("EMPLOYEE");

                    http.anyRequest().denyAll();
                })
                .addFilterBefore(jwtTokenValidator, BasicAuthenticationFilter.class)
                .build();
    }
}
