package com.cts.dtos;

import java.util.List;

public class RestaurantReportDto {

    private Long restaurantId;
    private Integer restaurantOrderCount;
    private List<OrderReportEntryDto> orders;

    public RestaurantReportDto() {}

    public RestaurantReportDto(Long restaurantId, Integer restaurantOrderCount, List<OrderReportEntryDto> orders) {
        this.restaurantId = restaurantId;
        this.restaurantOrderCount = restaurantOrderCount;
        this.orders = orders;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getRestaurantOrderCount() {
        return restaurantOrderCount;
    }

    public void setRestaurantOrderCount(Integer restaurantOrderCount) {
        this.restaurantOrderCount = restaurantOrderCount;
    }

    public List<OrderReportEntryDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderReportEntryDto> orders) {
        this.orders = orders;
    }
}