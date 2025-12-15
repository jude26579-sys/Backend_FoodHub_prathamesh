package com.cognizant.feedback.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.feedback.dto.FeedbackRequest;
import com.cognizant.feedback.model.Feedback;
import com.cognizant.feedback.repository.FeedbackRepository;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback submitFeedback(FeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setOrderId(request.getOrderId());
        feedback.setUserId(request.getUserId());
        feedback.setRestaurantId(request.getRestaurantId());
        feedback.setItemId(request.getItemId());
        feedback.setItemName(request.getItemName());
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    public List<Feedback> getFeedbackByRestaurantId(String restaurantId) {
        return feedbackRepository.findByRestaurantId(restaurantId);
    }

    public List<Feedback> getFeedbackByUserId(String userId) {
        return feedbackRepository.findByUserId(userId);
    }

    public List<Feedback> getFeedbackByItemId(String itemId) {
        return feedbackRepository.findByItemId(itemId);
    }


    public double getAverageRatingByRestaurant(String restaurantId) {
        List<Feedback> list = feedbackRepository.findByRestaurantId(restaurantId);
        return list.stream().mapToDouble(Feedback::getRating).average().orElse(0.0);
    }
    public double getAverageRatingByItem(String itemId) {
        List<Feedback> list = feedbackRepository.findByItemId(itemId);
        return list.stream().mapToDouble(Feedback::getRating).average().orElse(0.0);
    }

}
