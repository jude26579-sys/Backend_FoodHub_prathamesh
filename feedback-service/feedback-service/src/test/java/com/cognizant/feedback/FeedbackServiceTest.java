package com.cognizant.feedback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cognizant.feedback.dto.FeedbackRequest;
import com.cognizant.feedback.model.Feedback;
import com.cognizant.feedback.repository.FeedbackRepository;
import com.cognizant.feedback.service.FeedbackService;

public class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubmitFeedback() {
        FeedbackRequest request = new FeedbackRequest();
        request.setOrderId("O101");
        request.setUserId("U123");
        request.setRestaurantId("R001");
        request.setRating(5);
        request.setComment("Great food!");

        Feedback feedback = new Feedback();
        feedback.setOrderId("O101");
        feedback.setUserId("U123");
        feedback.setRestaurantId("R001");
        feedback.setRating(5);
        feedback.setComment("Great food!");

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        Feedback result = feedbackService.submitFeedback(request);

        assertEquals("U123", result.getUserId());
        assertEquals("R001", result.getRestaurantId());
        assertEquals(5, result.getRating());
        assertEquals("Great food!", result.getComment());
    }

    @Test
    public void testGetAllFeedback() {
        Feedback f1 = new Feedback();
        f1.setUserId("U123");
        f1.setRestaurantId("R001");
        f1.setRating(4);
        f1.setComment("Good food");

        Feedback f2 = new Feedback();
        f2.setUserId("U124");
        f2.setRestaurantId("R002");
        f2.setRating(5);
        f2.setComment("Excellent");

        when(feedbackRepository.findAll()).thenReturn(Arrays.asList(f1, f2));

        List<Feedback> feedbackList = feedbackService.getAllFeedback();

        assertEquals(2, feedbackList.size());
        assertEquals("Good food", feedbackList.get(0).getComment());
        assertEquals("Excellent", feedbackList.get(1).getComment());
    }

    @Test
    public void testGetAverageRatingByRestaurant() {
        String restaurantId = "R001";

        Feedback f1 = new Feedback();
        f1.setRestaurantId(restaurantId);
        f1.setRating(4);

        Feedback f2 = new Feedback();
        f2.setRestaurantId(restaurantId);
        f2.setRating(5);

        when(feedbackRepository.findByRestaurantId(restaurantId)).thenReturn(Arrays.asList(f1, f2));

        double avg = feedbackService.getAverageRatingByRestaurant(restaurantId);

        assertEquals(4.5, avg);
    }
}
