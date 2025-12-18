package com.notificationservice.kafka;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notificationservice.dto.OrderNotificationEvent;

@Component
public class NotificationPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationPublisher.class);

    private final SimpMessagingTemplate template;

    public NotificationPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Send notification to user (CUSTOMER or VENDOR) via WebSocket
     * 
     * @param userType Type of user: CUSTOMER or VENDOR
     * @param event OrderNotificationEvent containing order details
     */
    public void sendToUser(String userType, OrderNotificationEvent event) {
        if (userType == null || event == null) {
            logger.warn("⚠️ Cannot send notification: userType or event is null");
            return;
        }
        
        try {
            // CUSTOMER or VENDOR destination
            String destination = "/topic/" + userType.toLowerCase();
            logger.info("🔔 Publishing {} notification for order {} to destination: {}", 
                event.getStatus(), event.getOrderId(), destination);
            
            template.convertAndSend(destination, event);
            
            logger.info("✅ Notification sent to {} for order {} (Status: {})", 
                userType, event.getOrderId(), event.getStatus());
            
        } catch (Exception ex) {
            logger.error("❌ Error sending notification to {}: {}", userType, ex.getMessage(), ex);
        }
    }
}
