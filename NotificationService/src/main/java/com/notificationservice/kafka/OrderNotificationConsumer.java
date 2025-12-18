package com.notificationservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notificationservice.dto.OrderNotificationEvent;

@Component
public class OrderNotificationConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderNotificationConsumer.class);

    private final NotificationPublisher publisher;

    public OrderNotificationConsumer(NotificationPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Listen to vendor-notifications topic
     * Called when: Order is PLACED (notification goes to vendor)
     */
    @KafkaListener(
        topics = "vendor-notifications",
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "vendor-notification-group"
    )
    public void consumeVendorNotification(OrderNotificationEvent event) {
        logger.info("📨 Received vendor notification - Order: {}, Status: {}", 
            event.getOrderId(), event.getStatus());

        if (event == null || event.getOrderId() == null) {
            logger.warn("⚠️ Invalid event received: orderId is null");
            return;
        }

        logger.info("📤 Sending notification to VENDOR for order {}", event.getOrderId());
        // Push notification to vendor via WebSocket
        publisher.sendToUser(event.getTo(), event);
        logger.info("✅ Vendor notification sent for order {}", event.getOrderId());
    }

    /**
     * Listen to customer-notifications topic
     * Called when: Order is ACCEPTED or READY (notification goes to customer)
     */
    @KafkaListener(
        topics = "customer-notifications",
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "customer-notification-group"
    )
    public void consumeCustomerNotification(OrderNotificationEvent event) {
        logger.info("📨 Received customer notification - Order: {}, Status: {}", 
            event.getOrderId(), event.getStatus());

        if (event == null || event.getOrderId() == null) {
            logger.warn("⚠️ Invalid event received: orderId is null");
            return;
        }

        logger.info("📤 Sending notification to CUSTOMER for order {}", event.getOrderId());
        // Push notification to customer via WebSocket
        publisher.sendToUser(event.getTo(), event);
        logger.info("✅ Customer notification sent for order {}", event.getOrderId());
    }
}
