package com.cognizant.feedback.dto;

import jakarta.validation.constraints.*;

public class FeedbackRequest {

    @NotBlank(message = "Order ID cannot be empty")
    private String orderId;

    @NotBlank(message = "User ID cannot be empty")
    private String userId;

    @NotBlank(message = "Restaurant ID cannot be empty")
    private String restaurantId;

    private String itemId;

    private String itemName;

    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private double rating;  // changed from int → double

    @Size(max = 250, message = "Comment cannot exceed 250 characters")
    private String comment;

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
