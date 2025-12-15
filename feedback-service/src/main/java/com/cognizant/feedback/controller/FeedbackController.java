package com.cognizant.feedback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cognizant.feedback.dto.FeedbackRequest;
import com.cognizant.feedback.model.Feedback;
import com.cognizant.feedback.service.FeedbackService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public Feedback submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        return feedbackService.submitFeedback(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','VENDOR')")
    @GetMapping
    public List<Feedback> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','VENDOR')")
    @GetMapping("/restaurant/{restaurantId}")
    public List<Feedback> getFeedbackByRestaurant(@PathVariable Long restaurantId) {
        return feedbackService.getFeedbackByRestaurantId(restaurantId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','VENDOR')")
    @GetMapping("/user/{userId}")
    public List<Feedback> getFeedbackByUser(@PathVariable Long userId) {
        return feedbackService.getFeedbackByUserId(userId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','VENDOR')")
    @GetMapping("/item/{itemId}")
    public List<Feedback> getFeedbackByItem(@PathVariable Long itemId) {
        return feedbackService.getFeedbackByItemId(itemId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','VENDOR')")
    @GetMapping("/restaurant/{restaurantId}/average")
    public double getRestaurantAverageRating(@PathVariable Long restaurantId) {
        return feedbackService.getAverageRatingByRestaurant(restaurantId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','VENDOR')")
    @GetMapping("/item/{itemId}/average")
    public double getItemAverageRating(@PathVariable Long itemId) {
        return feedbackService.getAverageRatingByItem(itemId);
    }

   
}
