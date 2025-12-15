package com.cts.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    
    @JsonProperty("menuItemId")
    private Long menuItemId;
    
    @JsonProperty("name")
    private String itemName;
    
    @JsonProperty("unitPrice")
    private Double unitPrice;
    
    @JsonProperty("quantity")
    private Integer quantity;
    
    @JsonProperty("itemTotal")
    private Double itemTotal;
}
