package com.cts.clients;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Feign client to communicate with Restaurant Service
 */
@FeignClient(
    name = "RestaurantService", 
    url = "http://localhost:8182", 
    configuration = FeignClientInterceptor.class,
    fallback = RestaurantClientFallback.class
)
public interface RestaurantClient {

    /**
     * Fetch all restaurants from Restaurant Service
     * Endpoint: GET /api/restaurants
     * 
     * @return List of RestaurantDto objects
     */
    @GetMapping("/api/restaurants")
    List<RestaurantDto> getAllRestaurants();
    
    /**
     * Inner DTO class to match Restaurant Service's RestaurantResponse
     */
    public static class RestaurantDto {
        private Long restaurantId;
        private String restaurantName;
        private String location;
        private String openTime;
        private String closeTime;
        private String status;
        private Long vendorId;

        // Constructors
        public RestaurantDto() {}

        public RestaurantDto(Long restaurantId, String restaurantName, String location, 
                           String openTime, String closeTime, String status, Long vendorId) {
            this.restaurantId = restaurantId;
            this.restaurantName = restaurantName;
            this.location = location;
            this.openTime = openTime;
            this.closeTime = closeTime;
            this.status = status;
            this.vendorId = vendorId;
        }

        // Getters and Setters
        public Long getRestaurantId() {
            return restaurantId;
        }

        public void setRestaurantId(Long restaurantId) {
            this.restaurantId = restaurantId;
        }

        public String getRestaurantName() {
            return restaurantName;
        }

        public void setRestaurantName(String restaurantName) {
            this.restaurantName = restaurantName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public String getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(String closeTime) {
            this.closeTime = closeTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Long getVendorId() {
            return vendorId;
        }

        public void setVendorId(Long vendorId) {
            this.vendorId = vendorId;
        }

        @Override
        public String toString() {
            return "RestaurantDto [restaurantId=" + restaurantId + ", restaurantName=" + restaurantName
                    + ", location=" + location + ", status=" + status + "]";
        }
    }
}

/**
 * Fallback implementation for RestaurantClient
 * Used when Restaurant Service is unavailable
 */
@Component
class RestaurantClientFallback implements RestaurantClient {
    @Override
    public List<RestaurantDto> getAllRestaurants() {
        System.out.println("⚠️ RestaurantClient fallback triggered - Restaurant Service is unavailable");
        return new ArrayList<>();
    }
}

