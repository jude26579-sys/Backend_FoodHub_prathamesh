package com.cts.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class NotificationClient {

    private static final Logger logger = LoggerFactory.getLogger(NotificationClient.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String VENDOR_TOPIC = "vendor-notifications";
    private static final String CUSTOMER_TOPIC = "customer-notifications";

    public void notifyVendor(Long orderId, String message) {
        logger.info("📩 Sending Kafka message to vendor topic: Order ID {}: {}", orderId, message);
        kafkaTemplate.send(VENDOR_TOPIC, orderId.toString(), message);
    }

    public void notifyCustomer(Integer customerId, String message) {
        logger.info("📩 Sending Kafka message to customer topic: Customer ID {}: {}", customerId, message);
        kafkaTemplate.send(CUSTOMER_TOPIC, customerId.toString(), message);
    }
}