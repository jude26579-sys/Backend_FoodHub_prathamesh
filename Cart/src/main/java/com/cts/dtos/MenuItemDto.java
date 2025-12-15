package com.cts.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "itemId", "restaurantId", "categoryId", "itemName", "description", "isavailable", "price", "quantity", "totalItemPrice" })
public class MenuItemDto {
	
	private Long itemId;
	private String itemName;
    private String description;
    private Double price;
    private Boolean isavailable;
    private Long restaurantId;
    private Long categoryId;
    private int quantity;
    @Transient // Optional if not persisting
    private Double totalItemPrice;
}
