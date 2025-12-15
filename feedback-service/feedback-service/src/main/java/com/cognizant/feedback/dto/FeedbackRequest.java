package com.cognizant.feedback.dto;

import jakarta.validation.constraints.*;

public class FeedbackRequest {

    @NotBlank(message = "Order ID cannot be empty")
    private String orderId;

    @NotBlank(message = "User ID cannot be empty")
    private String userId;

    @NotBlank(message = "Restaurant ID cannot be empty")
    private String restaurantId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private int rating;

    @Size(max = 250, message = "Comment cannot exceed 250 characters")
    private String comment;

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
