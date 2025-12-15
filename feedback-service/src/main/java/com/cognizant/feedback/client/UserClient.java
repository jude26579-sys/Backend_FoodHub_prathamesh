package com.cognizant.feedback.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://localhost:8080",configuration = FeignClientInterceptor.class) // replace with your port
public interface UserClient {

    @GetMapping("/customer/{id}")
    Object getUserById(@PathVariable Long id);
}
