package com.cts.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderNotificationEvent {

    private Long orderId;
    
    // PLACED | ACCEPTED | READY | CANCELLED
    private String status;

    // CUSTOMER | VENDOR
    private String from;

    // CUSTOMER | VENDOR
    private String to;

    private Long customerId;
    private Long vendorId;

    private LocalDateTime timestamp;

}