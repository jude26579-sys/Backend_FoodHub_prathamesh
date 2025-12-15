package com.cts.clients;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.dtos.OrdersDto;

/**
 * Feign client to communicate with PlacingOrder Service
 * Retrieves order data for reporting purposes
 */
@FeignClient(
    name = "PlacingOrder", 
    url = "http://localhost:8083",
    configuration = FeignClientInterceptor.class,
    fallback = ReportingClientFallback.class
)
public interface ReportingClient {

    /**
     * Get all orders (no filter)
     * Endpoint: GET /api/orders
     * 
     * @return List of all orders
     */
    @GetMapping("/api/orders")
    List<OrdersDto> getAllOrders();

    /**
     * Get orders by date range and restaurant IDs
     * This is a simple wrapper that will filter results client-side
     * 
     * @param startDate Start date in yyyy-MM-dd format
     * @param endDate End date in yyyy-MM-dd format
     * @param restaurantIds Comma-separated restaurant IDs
     * @return List of orders matching the criteria
     */
    @GetMapping("/api/orders")
    List<OrdersDto> getOrdersByDateAndRestaurantIds(
        @RequestParam("start") String startDate,
        @RequestParam("end") String endDate,
        @RequestParam("restaurantIds") List<Long> restaurantIds
    );
}

/**
 * Fallback implementation for ReportingClient
 * Used when PlacingOrder Service is unavailable
 */
@Component
class ReportingClientFallback implements ReportingClient {
    @Override
    public List<OrdersDto> getAllOrders() {
        System.out.println("⚠️ ReportingClient fallback triggered - PlacingOrder Service is unavailable");
        return new ArrayList<>();
    }

    @Override
    public List<OrdersDto> getOrdersByDateAndRestaurantIds(String startDate, String endDate, List<Long> restaurantIds) {
        System.out.println("⚠️ ReportingClient fallback triggered - PlacingOrder Service is unavailable");
        return new ArrayList<>();
    }
}

