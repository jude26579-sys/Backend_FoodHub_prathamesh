package com.cts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cts.clients.NotificationClient;
import com.cts.clients.PaymentClient;
import com.cts.entities.OrderStatus;
import com.cts.entities.Orders;
import com.cts.repository.OrderRepository;

@Component
public class OrderSagaOrchestrator {

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private NotificationClient notificationClient;

    public void processOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        try {
            // Step 1: Initiate Payment
            boolean paymentSuccess = paymentClient.processPayment(order.getOrderId(), order.getSubTotal());

            if (paymentSuccess) {
                // Step 2: Update Order Status to CONFIRMED
                order.setOrderStatus(OrderStatus.CONFIRMED);
                orderRepository.save(order);

                // Notify Vendor
                notificationClient.notifyVendor(order.getOrderId(), "Order Placed");

                // Step 3: Vendor Accepts Order
                order.setOrderStatus(OrderStatus.ACCEPTED);
                orderRepository.save(order);
                notificationClient.notifyCustomer(order.getCustomerId(), "Order Accepted by Vendor");

                // Step 4: Vendor Marks Order as Ready
                order.setOrderStatus(OrderStatus.ORDER_READY);
                orderRepository.save(order);
                notificationClient.notifyCustomer(order.getCustomerId(), "Order is Ready for Pickup/Delivery");

            } else {
                // Step 5: Compensating Action
                order.setOrderStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
                notificationClient.notifyCustomer(order.getCustomerId(), "Order Cancelled due to Payment Failure");
            }
        } catch (Exception ex) {
            // Handle unexpected failures
            order.setOrderStatus(OrderStatus.PAYMENT_FAILED);
            orderRepository.save(order);
            notificationClient.notifyCustomer(order.getCustomerId(), "Order Payment Failed. Please Retry.");
        }
    }
}