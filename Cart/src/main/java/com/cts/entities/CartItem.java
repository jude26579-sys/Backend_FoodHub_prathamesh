package com.cts.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long itemId;
    
    private String itemName;
    private String description;
    private Double price;
    private Boolean isavailable;
    private Long restaurantId;
    private Long categoryId;
    private int quantity;
    private double totalItemPrice;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

	
}