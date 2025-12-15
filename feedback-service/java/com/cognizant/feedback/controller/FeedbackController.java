package com.cognizant.feedback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cognizant.feedback.dto.FeedbackRequest;
import com.cognizant.feedback.model.Feedback;
import com.cognizant.feedback.service.FeedbackService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public Feedback submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        return feedbackService.submitFeedback(request);
    }

    @GetMapping
    public List<Feedback> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<Feedback> getFeedbackByRestaurant(@PathVariable String restaurantId) {
        return feedbackService.getFeedbackByRestaurantId(restaurantId);
    }

    @GetMapping("/user/{userId}")
    public List<Feedback> getFeedbackByUser(@PathVariable String userId) {
        return feedbackService.getFeedbackByUserId(userId);
    }

    @GetMapping("/item/{itemId}")
    public List<Feedback> getFeedbackByItem(@PathVariable String itemId) {
        return feedbackService.getFeedbackByItemId(itemId);
    }

    @GetMapping("/restaurant/{restaurantId}/average")
    public double getRestaurantAverageRating(@PathVariable String restaurantId) {
        return feedbackService.getAverageRatingByRestaurant(restaurantId);
    }

    @GetMapping("/item/{itemId}/average")
    public double getItemAverageRating(@PathVariable String itemId) {
        return feedbackService.getAverageRatingByItem(itemId);
    }

   
}
