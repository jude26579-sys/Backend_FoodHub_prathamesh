package com.cognizant.feedback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cognizant.feedback.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByRestaurantId(String restaurantId);
    List<Feedback> findByUserId(String userId);
    List<Feedback> findByItemId(String itemId);
}
