package com.cognizant.feedback.service;
 
import com.cognizant.feedback.client.OrderClient;
import com.cognizant.feedback.client.RestaurantClient;
import com.cognizant.feedback.client.UserClient;
import com.cognizant.feedback.dto.FeedbackRequest;
import com.cognizant.feedback.model.Feedback;
import com.cognizant.feedback.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
public class FeedbackService {
 
    @Autowired
    private FeedbackRepository feedbackRepository;
 
    @Autowired
    private UserClient userClient;
 
    @Autowired
    private OrderClient orderClient;
 
    @Autowired
    private RestaurantClient restaurantClient;
 
    public Feedback submitFeedback(FeedbackRequest request) {
 
        // Validate User
        try {
            userClient.getUserById(request.getUserId());
        } catch (Exception e) {
            throw new RuntimeException("Invalid User ID: " + request.getUserId());
        }
 
        // Validate Order
        try {
            orderClient.getOrderById(request.getOrderId());
        } catch (Exception e) {
            throw new RuntimeException("Invalid Order ID: " + request.getOrderId());
        }
 
        // Validate Restaurant
        try {
            restaurantClient.getRestaurantById(request.getRestaurantId());
        } catch (Exception e) {
            throw new RuntimeException("Invalid Restaurant ID: " + request.getRestaurantId());
        }
 
        //  CHECK IF FEEDBACK ALREADY EXISTS FOR:
        // SAME orderId + SAME userId + SAME itemId
        Feedback existingFeedback =
                feedbackRepository.findByOrderIdAndUserIdAndItemId(
                        request.getOrderId(),
                        request.getUserId(),
                        request.getItemId() // null for restaurant-level rating → still works
                );
 
        Feedback feedback;
 
        if (existingFeedback != null) {
            // UPDATE existing feedback
            feedback = existingFeedback;
            feedback.setRating(request.getRating());
            feedback.setComment(request.getComment());
            feedback.setItemName(request.getItemName()); // optional item name update
        } else {
            //  CREATE NEW feedback
            feedback = new Feedback();
            feedback.setOrderId(request.getOrderId());
            feedback.setUserId(request.getUserId());
            feedback.setRestaurantId(request.getRestaurantId());
            feedback.setItemId(request.getItemId());
            feedback.setItemName(request.getItemName());
            feedback.setRating(request.getRating());
            feedback.setComment(request.getComment());
        }
 
        return feedbackRepository.save(feedback);
    }
 
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
 
    public List<Feedback> getFeedbackByRestaurantId(Long restaurantId) {
        return feedbackRepository.findByRestaurantId(restaurantId);
    }
 
    public List<Feedback> getFeedbackByUserId(Long userId) {
        return feedbackRepository.findByUserId(userId);
    }
 
    public List<Feedback> getFeedbackByItemId(Long itemId) {
        return feedbackRepository.findByItemId(itemId);
    }
 
    public double getAverageRatingByRestaurant(Long restaurantId) {
        List<Feedback> list = feedbackRepository.findByRestaurantId(restaurantId);
        return list.stream().mapToDouble(Feedback::getRating).average().orElse(0.0);
    }
 
    public double getAverageRatingByItem(Long itemId) {
        List<Feedback> list = feedbackRepository.findByItemId(itemId);
        return list.stream().mapToDouble(Feedback::getRating).average().orElse(0.0);
    }
}
 
 