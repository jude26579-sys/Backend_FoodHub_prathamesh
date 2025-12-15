package com.cts.dtos;

import java.time.LocalDateTime;

public class OrderReportEntryDto {

    private Long orderId;
    private Double orderSalesAmt;
    private String orderStatus;
    private LocalDateTime createdAt;

    public OrderReportEntryDto() {}

    public OrderReportEntryDto(Long orderId, Double orderSalesAmt, String orderStatus, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderSalesAmt = orderSalesAmt;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
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
}