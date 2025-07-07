package com.plazoleta.foodcourtmicroservice.application.client.handler;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.plazoleta.foodcourtmicroservice.application.client.dto.UserInfoResponse;

@FeignClient(name = "user-microservice", url = "${user-microservice.url}")
public interface UserHandlerClient {

    @GetMapping("/api/v1/user/{id}")
    UserInfoResponse getUserInfobyId(@PathVariable Long id);

}
