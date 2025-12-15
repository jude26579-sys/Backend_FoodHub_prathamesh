package com.cts.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class OrderReportEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Double orderSalesAmt;
    private String orderStatus;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "restaurant_report_id")
    private RestaurantReport restaurantReport;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getOrderSalesAmt() {
        return orderSalesAmt;
    }

    public void setOrderSalesAmt(Double orderSalesAmt) {
        this.orderSalesAmt = orderSalesAmt;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public RestaurantReport getRestaurantReport() {
        return restaurantReport;
    }

    public void setRestaurantReport(RestaurantReport restaurantReport) {
        this.restaurantReport = restaurantReport;
    }
}