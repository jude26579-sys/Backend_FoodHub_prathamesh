package com.cognizant.feedback.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FeedbackRequest {

    @NotNull(message = "Order ID cannot be empty")
    private Long orderId;

    @NotNull(message = "User ID cannot be empty")
    private Long userId;

    @NotNull(message = "Restaurant ID cannot be empty")
    private Long restaurantId;

    private Long itemId;

    private String itemName;

    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private double rating;

    @Size(max = 250, message = "Comment cannot exceed 250 characters")
    private String comment;
}
