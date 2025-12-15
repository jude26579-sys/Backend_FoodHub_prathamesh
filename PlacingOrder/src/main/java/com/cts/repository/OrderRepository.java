package com.cts.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.entities.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    
    /**
     * Find all orders by customer ID
     * @param customerId Customer ID
     * @return List of orders for the customer
     */
    List<Orders> findByCustomerId(Long customerId);
}
