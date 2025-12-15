package com.cognizant.feedback.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "restaurant-service", url = "http://localhost:8182",configuration = FeignClientInterceptor.class)
public interface RestaurantClient {

    @GetMapping("/api/restaurants/{id}")
    Object getRestaurantById(@PathVariable Long id);
}
