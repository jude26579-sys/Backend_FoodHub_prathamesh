package com.cts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;



import java.util.List;
 
@Data
public class InventoryUpdateRequest {
    private Long restaurantId;            
    private List<InventoryUpdateItem> items;
}
