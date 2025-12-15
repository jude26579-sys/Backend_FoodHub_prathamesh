package com.cognizant.feedback.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.feedback.dto.FeedbackRequest;
import com.cognizant.feedback.model.Feedback;
import com.cognizant.feedback.repository.FeedbackRepository;
import com.cognizant.logging.JsonLogger;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // Initialize JSON logger
    private static final JsonLogger logger = new JsonLogger(FeedbackService.class);

    public Feedback submitFeedback(FeedbackRequest request) {
        Map<String, Object> details = new HashMap<>();
        details.put("orderId", request.getOrderId());
        details.put("userId", request.getUserId());
        details.put("restaurantId", request.getRestaurantId());
        details.put("rating", request.getRating());

        logger.info("Submitting feedback", details);

        Feedback feedback = new Feedback();
        feedback.setOrderId(request.getOrderId());
        feedback.setUserId(request.getUserId());
        feedback.setRestaurantId(request.getRestaurantId());
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());

        try {
            Feedback saved = feedbackRepository.save(feedback);
            logger.info("Feedback submitted successfully", details);
            return saved;
        } catch (Exception e) {
            logger.error("Error submitting feedback", details);
            throw e;
        }
    }

    public List<Feedback> getAllFeedback() {
        logger.info("Fetching all feedback", null);
        return feedbackRepository.findAll();
    }

    public List<Feedback> getFeedbackByRestaurantId(String restaurantId) {
        logger.info("Fetching feedback by restaurantId", Map.of("restaurantId", restaurantId));
        return feedbackRepository.findByRestaurantId(restaurantId);
    }

    public List<Feedback> getFeedbackByUserId(String userId) {
        logger.info("Fetching feedback by userId", Map.of("userId", userId));
        return feedbackRepository.findByUserId(userId);
    }

    public double getAverageRatingByRestaurant(String restaurantId) {
        logger.info("Calculating average rating for restaurant", Map.of("restaurantId", restaurantId));
        List<Feedback> list = feedbackRepository.findByRestaurantId(restaurantId);
        double avg = list.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
        logger.info("Average rating calculated", Map.of("restaurantId", restaurantId, "averageRating", avg));
        return avg;
    }
}
