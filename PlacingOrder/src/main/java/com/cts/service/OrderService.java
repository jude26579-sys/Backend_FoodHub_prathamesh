
package com.cts.service;

import java.util.List;
import com.cts.dtos.OrdersDto;
import com.cts.entities.OrdersRequest;

public interface OrderService {
    
    List<OrdersDto> getAllOrders();
    
    OrdersDto getOrdersById(Long orderId);
    
    /**
     * Get all orders for a specific customer
     * @param customerId Customer ID
     * @return List of orders for the customer
     */
    List<OrdersDto> getOrdersByCustomerId(Long customerId);
    
    OrdersDto addOrders(OrdersRequest request);
    
    /**
     * Auto-update order status based on payment status
     * Called directly by Payment Service via Feign
     * 
     * @param orderId Order ID
     * @param paymentStatus Payment status (SUCCESS, FAILED, REFUNDED)
     * @return Updated OrdersDto
     */
    OrdersDto updateOrderStatusByPayment(Long orderId, String paymentStatus);
    
    /**
     * Legacy update - fetches payment status from Payment Service
     */
    void updateOrderStatus(Long orderId);
    
    /**
     * Update order status from vendor (ACCEPTED, READY)
     * Sends notification to customer via Kafka
     * 
     * @param orderId Order ID
     * @param newStatus New status (ACCEPTED or READY)
     * @return Updated OrdersDto
     */
    OrdersDto updateOrderStatusByVendor(Long orderId, String newStatus);
    
    void deleteOrder(Long orderId);
}
