package com.cts.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.cts.entities.Duration;

public class OverallReportDto {

    private String reportId;
    private Duration duration;
    private List<RestaurantReportDto> restaurants;
    private Double totalSales;
    private Integer restaurantCount;
    private Integer orderCount;
    private LocalDateTime reportGeneratedAt;

    public OverallReportDto() {}

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public List<RestaurantReportDto> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<RestaurantReportDto> restaurants) {
        this.restaurants = restaurants;
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Double totalSales) {
        this.totalSales = totalSales;
    }

    public Integer getRestaurantCount() {
        return restaurantCount;
    }

    public void setRestaurantCount(Integer restaurantCount) {
        this.restaurantCount = restaurantCount;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public LocalDateTime getReportGeneratedAt() {
        return reportGeneratedAt;
    }

    public void setReportGeneratedAt(LocalDateTime reportGeneratedAt) {
        this.reportGeneratedAt = reportGeneratedAt;
    }
}