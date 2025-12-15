package com.cognizant.feedback.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cognizant.feedback.dto.FeedbackRequest;
import com.cognizant.feedback.model.Feedback;
import com.cognizant.feedback.service.FeedbackService;
import com.cognizant.logging.JsonLogger;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // Initialize JSON logger
    private static final JsonLogger logger = new JsonLogger(FeedbackController.class);

    @PostMapping
    public Feedback submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        logger.info("Received request to submit feedback", Map.of(
                "userId", request.getUserId(),
                "restaurantId", request.getRestaurantId(),
                "orderId", request.getOrderId(),
                "rating", request.getRating()
        ));
        return feedbackService.submitFeedback(request);
    }

    @GetMapping
    public List<Feedback> getAllFeedback() {
        logger.info("Received request to fetch all feedback", null);
        return feedbackService.getAllFeedback();
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<Feedback> getFeedbackByRestaurant(@PathVariable String restaurantId) {
        logger.info("Received request to fetch feedback by restaurant", Map.of("restaurantId", restaurantId));
        return feedbackService.getFeedbackByRestaurantId(restaurantId);
    }

    @GetMapping("/user/{userId}")
    public List<Feedback> getFeedbackByUser(@PathVariable String userId) {
        logger.info("Received request to fetch feedback by user", Map.of("userId", userId));
        return feedbackService.getFeedbackByUserId(userId);
    }

    @GetMapping("/restaurant/{restaurantId}/average")
    public double getRestaurantAverageRating(@PathVariable String restaurantId) {
        logger.info("Received request to fetch average rating for restaurant", Map.of("restaurantId", restaurantId));
        double avg = feedbackService.getAverageRatingByRestaurant(restaurantId);
        logger.info("Average rating returned", Map.of("restaurantId", restaurantId, "averageRating", avg));
        return avg;
    }

    @GetMapping("/")
    public String home() {
        logger.info("Health check endpoint called", null);
        return "Feedback Service is running!";
    }
}
