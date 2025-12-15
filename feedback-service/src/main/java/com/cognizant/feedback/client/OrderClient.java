package com.cognizant.feedback.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "http://localhost:8083",configuration = FeignClientInterceptor.class)
public interface OrderClient {

    @GetMapping("/api/orders/{orderId}")
    Object getOrderById(@PathVariable Long orderId);
}
