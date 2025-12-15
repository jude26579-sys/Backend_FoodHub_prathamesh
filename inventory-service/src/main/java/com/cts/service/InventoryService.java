package com.cts.service;

import java.util.List;

import com.cts.dto.InventoryDto;
import com.cts.dto.InventoryUpdateRequest;

public interface InventoryService {
		InventoryDto getIventoryById(Long id);
		List<InventoryDto> getAllInventory();
		List<InventoryDto> getInventoryByRestaurantId(Long restaurantId);
		InventoryDto addInventory(InventoryDto dto);
		InventoryDto updateInventory( Long id,InventoryDto dto);
		void deleteInventory(Long id);
		//void reduceInventoryAfterOrder(Long restaurantId, Long categoryId, Long itemId, int quantityOrdered);
		void updateInventory(InventoryUpdateRequest req);
		
}
