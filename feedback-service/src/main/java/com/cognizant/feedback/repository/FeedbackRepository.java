package com.cognizant.feedback.repository;
 
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cognizant.feedback.model.Feedback;
 
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByRestaurantId(Long restaurantId);
    List<Feedback> findByUserId(Long userId);
    List<Feedback> findByItemId(Long itemId);
    
    Feedback findByOrderIdAndUserIdAndItemId(Long orderId, Long userId, Long itemId);
 
}