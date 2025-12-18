# Kafka Notification Flow - Implementation Guide

## Overview

The system now implements a complete Kafka-based notification workflow:

- **Order Placed** → Notification sent to VENDOR
- **Order Accepted** → Notification sent to CUSTOMER
- **Order Ready** → Notification sent to CUSTOMER

---

## Architecture

### Kafka Topics

- **vendor-notifications**: Orders placed by customers (PLACED status)
- **customer-notifications**: Order status updates from vendor (ACCEPTED, READY)

### Services

#### PlacingOrder Service

- **Produces** messages to `vendor-notifications` when order is PLACED
- **Produces** messages to `customer-notifications` when vendor updates status (ACCEPTED/READY)

#### NotificationService

- **Consumes** from both `vendor-notifications` and `customer-notifications`
- **Broadcasts** notifications via WebSocket to connected clients

---

## Complete Flow

### 1. Order Placement (Customer → Vendor)

```
Customer creates order
    ↓
PlacingOrder Service saves order with status PLACED
    ↓
OrderNotificationEvent published to vendor-notifications topic:
{
  "orderId": 1,
  "status": "PLACED",
  "from": "CUSTOMER",
  "to": "VENDOR",
  "customerId": 123,
  "vendorId": 456,
  "timestamp": "2025-12-18T10:30:00"
}
    ↓
NotificationService consumes from vendor-notifications
    ↓
NotificationPublisher sends to /topic/vendor WebSocket
    ↓
Vendor receives real-time notification
```

### 2. Order Acceptance (Vendor → Customer)

```
Vendor accepts order via API:
PUT /api/orders/{orderId}/vendor-status
{
  "orderId": 1,
  "status": "ACCEPTED"
}
    ↓
OrderServiceImpl.updateOrderStatusByVendor() is called
    ↓
Order status updated to ACCEPTED
    ↓
OrderNotificationEvent published to customer-notifications topic:
{
  "orderId": 1,
  "status": "ACCEPTED",
  "from": "VENDOR",
  "to": "CUSTOMER",
  "customerId": 123,
  "vendorId": 456,
  "timestamp": "2025-12-18T10:35:00"
}
    ↓
NotificationService consumes from customer-notifications
    ↓
NotificationPublisher sends to /topic/customer WebSocket
    ↓
Customer receives real-time notification
```

### 3. Order Ready (Vendor → Customer)

```
Vendor marks order as ready via API:
PUT /api/orders/{orderId}/vendor-status
{
  "orderId": 1,
  "status": "READY"
}
    ↓
OrderServiceImpl.updateOrderStatusByVendor() is called
    ↓
Order status updated to READY
    ↓
OrderNotificationEvent published to customer-notifications topic:
{
  "orderId": 1,
  "status": "READY",
  "from": "VENDOR",
  "to": "CUSTOMER",
  "customerId": 123,
  "vendorId": 456,
  "timestamp": "2025-12-18T10:40:00"
}
    ↓
NotificationService consumes from customer-notifications
    ↓
NotificationPublisher sends to /topic/customer WebSocket
    ↓
Customer receives real-time notification
```

---

## API Endpoints

### 1. Create Order (Customer)

```
POST /api/orders
Content-Type: application/json
Authorization: Bearer {CUSTOMER_TOKEN}

{
  "customerId": 123,
  "restaurantId": 456,
  "cartId": 789
}

Response: 201 Created
{
  "orderId": 1,
  "orderStatus": "PLACED",
  "customerId": 123,
  "restaurantId": 456,
  "cartId": 789,
  "subTotal": 250.00,
  "createdAt": "2025-12-18T10:30:00",
  "updatedAt": "2025-12-18T10:30:00",
  "items": [...]
}
```

### 2. Update Order Status by Vendor

```
PUT /api/orders/{orderId}/vendor-status
Content-Type: application/json
Authorization: Bearer {VENDOR_TOKEN}

{
  "orderId": 1,
  "status": "ACCEPTED"  // or "READY"
}

Response: 200 OK
{
  "orderId": 1,
  "orderStatus": "ACCEPTED",
  "customerId": 123,
  "restaurantId": 456,
  "cartId": 789,
  "subTotal": 250.00,
  "createdAt": "2025-12-18T10:30:00",
  "updatedAt": "2025-12-18T10:35:00",
  "items": [...]
}
```

---

## WebSocket Subscriptions (For Clients)

### Vendor Receiving Notifications

```javascript
// Connect to WebSocket
const socket = new SockJS("/ws");
const stompClient = Stomp.over(socket);

// Subscribe to vendor notifications
stompClient.subscribe("/topic/vendor", function (message) {
  const notification = JSON.parse(message.body);
  console.log("New order received:", notification);
  // {
  //   "orderId": 1,
  //   "status": "PLACED",
  //   "from": "CUSTOMER",
  //   "to": "VENDOR",
  //   "customerId": 123,
  //   "vendorId": 456,
  //   "timestamp": "2025-12-18T10:30:00"
  // }
});
```

### Customer Receiving Notifications

```javascript
// Subscribe to customer notifications
stompClient.subscribe("/topic/customer", function (message) {
  const notification = JSON.parse(message.body);
  console.log("Order status update:", notification);
  // {
  //   "orderId": 1,
  //   "status": "ACCEPTED" or "READY",
  //   "from": "VENDOR",
  //   "to": "CUSTOMER",
  //   "customerId": 123,
  //   "vendorId": 456,
  //   "timestamp": "2025-12-18T10:35:00"
  // }
});
```

---

## Testing Steps

### Prerequisites

1. Kafka is running on `localhost:9092`
2. Both PlacingOrder and NotificationService are running
3. Have API testing tool (Postman/curl) and WebSocket client ready

### Test Case 1: Order Placement Notification

1. **Open WebSocket connection** for vendor in one browser tab
   - Connect to WebSocket endpoint
   - Subscribe to `/topic/vendor`
2. **Create order** via API (PlacingOrder Service)
   ```bash
   curl -X POST http://localhost:8080/api/orders \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer {CUSTOMER_TOKEN}" \
     -d '{
       "customerId": 123,
       "restaurantId": 456,
       "cartId": 789
     }'
   ```
3. **Expected Result**: Vendor should receive notification on WebSocket with status "PLACED"

### Test Case 2: Order Acceptance Notification

1. **Keep vendor WebSocket** from Test Case 1 open

2. **Open WebSocket connection** for customer in another browser tab

   - Connect to WebSocket endpoint
   - Subscribe to `/topic/customer`

3. **Vendor accepts order** via API (PlacingOrder Service)

   ```bash
   curl -X PUT http://localhost:8080/api/orders/1/vendor-status \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer {VENDOR_TOKEN}" \
     -d '{
       "orderId": 1,
       "status": "ACCEPTED"
     }'
   ```

4. **Expected Result**: Customer should receive notification on WebSocket with status "ACCEPTED"

### Test Case 3: Order Ready Notification

1. **Keep both WebSocket connections** from previous tests

2. **Vendor marks order as ready** via API

   ```bash
   curl -X PUT http://localhost:8080/api/orders/1/vendor-status \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer {VENDOR_TOKEN}" \
     -d '{
       "orderId": 1,
       "status": "READY"
     }'
   ```

3. **Expected Result**: Customer should receive notification on WebSocket with status "READY"

---

## Logging

### PlacingOrder Service Logs

```
✅ Order saved with ID: 1, Status: PLACED
📤 PLACED notification sent to vendor for order 1
✅ Kafka sent to topic=vendor-notifications, partition=0, offset=0
```

### NotificationService Logs

```
📨 Received vendor notification - Order: 1, Status: PLACED
📤 Sending notification to VENDOR for order 1
✅ Vendor notification sent for order 1
📤 Sending notification to VENDOR for order 1
✅ VENDOR notification sent via WebSocket
```

---

## Troubleshooting

### Issue: Notifications not being received

**Solutions:**

1. Verify Kafka is running: `kafka-topics.sh --list --bootstrap-server localhost:9092`
2. Check if topics exist: `kafka-topics.sh --describe --bootstrap-server localhost:9092`
3. Verify consumer group: Check NotificationService logs for consumer group connections
4. Check WebSocket connection: Ensure client is properly subscribed to correct topic

### Issue: Kafka topics not created

**Solutions:**

1. The topics should auto-create with KafkaTopicConfig beans
2. Manually create if needed:
   ```bash
   kafka-topics.sh --create --topic vendor-notifications --partitions 3 --replication-factor 1 --bootstrap-server localhost:9092
   kafka-topics.sh --create --topic customer-notifications --partitions 3 --replication-factor 1 --bootstrap-server localhost:9092
   ```

### Issue: Messages not being consumed

**Solutions:**

1. Check consumer configuration in KafkaConsumerConfig
2. Verify group IDs are correct in @KafkaListener annotations
3. Check if offset is set correctly (AUTO_OFFSET_RESET_CONFIG)
4. Review logs for deserialization errors

---

## Key Classes Modified

### PlacingOrder Service

- `OrderServiceImpl.java`: Added `updateOrderStatusByVendor()` method, modified notification logic
- `OrderService.java`: Added new interface method
- `OrderController.java`: Added `/vendor-status` endpoint
- `KafkaProducerConfig.java`: Added topic configuration beans
- **NEW**: `OrderStatusUpdateRequest.java`: DTO for vendor status updates

### NotificationService

- `OrderNotificationConsumer.java`: Updated to listen to both topics with separate methods
- `NotificationPublisher.java`: Enhanced logging

---

## Notes

- Ensure PlacingOrder and NotificationService use the same OrderNotificationEvent DTO
- Both services have correct Kafka bootstrap server configuration (localhost:9092)
- WebSocket endpoints should be properly configured in both services
- Customer and Vendor roles should be properly defined in your security configuration
