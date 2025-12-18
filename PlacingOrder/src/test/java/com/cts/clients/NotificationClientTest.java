// package com.cts.clients;

// import com.cts.config.KafkaProducerConfig;
// import org.apache.kafka.clients.producer.MockProducer;
// import org.apache.kafka.clients.producer.ProducerRecord;
// import org.apache.kafka.common.serialization.StringSerializer;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.kafka.core.KafkaTemplate;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.*;

// class NotificationClientTest {

//     @Mock
//     private KafkaTemplate<String, Object> kafkaTemplate;

//     @InjectMocks
//     private NotificationClient notificationClient;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testNotifyVendor() {
//         Long orderId = 123L;
//         String message = "Order Placed";

//         notificationClient.notifyVendor(orderId, message);

//         verify(kafkaTemplate, times(1)).send("vendor-notifications", orderId.toString(), message);
//     }

//     @Test
//     void testNotifyCustomer() {
//         Integer customerId = 456;
//         String message = "Order Ready";

//         notificationClient.notifyCustomer(customerId, message);

//         verify(kafkaTemplate, times(1)).send("customer-notifications", customerId.toString(), message);
//     }
// }