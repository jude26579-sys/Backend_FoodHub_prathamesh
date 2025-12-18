package com.notificationservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic customerNotificationsTopic() {
        return TopicBuilder.name("customer-notifications")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic vendorNotificationsTopic() {
        return TopicBuilder.name("vendor-notifications")
                .partitions(3)
                .replicas(1)
                .build();
    }
}