package com.cts.entity;

import com.cts.client.CategoryResponse;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory_items",uniqueConstraints = @UniqueConstraint(columnNames = {"itemName", "restaurantId", "categoryId"}))
public class Inventory {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long inventoryId;
	    private String itemName;           
	    private Integer quantityAvailable; 
	    private Integer reorderThreshold; 
	    private Long itemId;
	    private Long restaurantId;
	    private Long categoryId; 
	}

