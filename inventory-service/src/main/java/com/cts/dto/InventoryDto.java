package com.cts.dto;

import com.cts.validation.Validation.Create;
import com.cts.validation.Validation.Update;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InventoryDto {
	 private Long inventoryId;
	    @NotBlank(message = "Item name must not be blank",groups = { Create.class, Update.class })
	    private String itemName;
	    @NotNull(message = "Quantity available is required",groups = { Create.class, Update.class })
	    @Min(value = 0, message = "Quantity available must be zero or more",groups = { Create.class, Update.class })
	    private Integer quantityAvailable;
	    @NotNull(message = "Reorder threshold is required",groups = { Create.class, Update.class })
	    @Min(value = 0, message = "Reorder threshold must be zero or more",groups = { Create.class, Update.class })
	    private Integer reorderThreshold;
	    @NotNull(message = "itemId is required",groups = { Create.class, Update.class })
	    private Long itemId;
	    @NotNull(message = "resturantId  is required",groups = { Create.class, Update.class })
	    private Long restaurantId;
	    @NotNull(message = "categoryId  is required",groups = { Create.class, Update.class })
	    private Long categoryId;
	  
}
