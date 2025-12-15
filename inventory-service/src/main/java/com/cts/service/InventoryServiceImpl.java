package com.cts.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.client.MenuClient;
import com.cts.client.MenuResponse;
import com.cts.dto.InventoryDto;
import com.cts.dto.InventoryUpdateItem;
import com.cts.dto.InventoryUpdateRequest;
import com.cts.entity.Inventory;
import com.cts.exception.DuplicateException;
import com.cts.exception.ResourceNotFoundException;
import com.cts.repository.InventoryRepository;

import jakarta.transaction.Transactional;
@Service
public class InventoryServiceImpl implements InventoryService {
	@Autowired
	 public InventoryRepository inventoryRepository;
	@Autowired
	public MenuClient menuclient;
	@Override
	public InventoryDto getIventoryById(Long id) {
		Inventory inv=inventoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Inventory not found"));
		return convertEntityToDto(inv);
	}

	@Override
	public InventoryDto addInventory(InventoryDto dto) {
		MenuResponse menuresponse=menuclient.getMenuItem(dto.getRestaurantId(),null,dto.getCategoryId(), null,dto.getItemId(),dto.getItemName());
		Boolean exist=inventoryRepository.existsByRestaurantIdAndCategoryIdAndItemId(dto.getRestaurantId(), dto.getCategoryId(),dto.getItemId());
		if(exist) {
			throw new DuplicateException("Inventory already defined for Item in this restaurant and category");
		}
		Inventory inv=new Inventory();
		inv.setInventoryId(dto.getInventoryId());
		inv.setRestaurantId(menuresponse.getRestaurantId());
		inv.setCategoryId(menuresponse.getCategoryId());
		inv.setItemId(menuresponse.getItemId());
		inv.setItemName(menuresponse.getItemName());
		inv.setQuantityAvailable(dto.getQuantityAvailable());
		inv.setReorderThreshold(dto.getReorderThreshold());
		Inventory inventory=inventoryRepository.save(inv);
		return convertEntityToDto(inventory);
	}
//	@Override
//	public void reduceInventoryAfterOrder(Long restaurantId, Long categoryId, Long itemId, int quantityOrdered) {
//	    MenuResponse menuItem = menuclient.getMenuItemForInventory(restaurantId, categoryId, itemId);
//
//	    Inventory inventory = inventoryRepository.findByItemIdAndRestaurantIdAndCategoryId(
//	            menuItem.getItemId(), menuItem.getRestaurantId(), menuItem.getCategoryId()
//	    ).orElseThrow(() -> new ResourceNotFoundException("Inventory item not found"));
//
//	    if (inventory.getQuantityAvailable() < quantityOrdered) {
//	        throw new IllegalStateException("Not enough inventory available");
//	    }
//
//	    inventory.setQuantityAvailable(inventory.getQuantityAvailable() - quantityOrdered);
//	    inventoryRepository.save(inventory);
//	}
	@Override
	public InventoryDto updateInventory(Long id ,InventoryDto dto) {
	    Inventory existing = inventoryRepository.findById(dto.getInventoryId())
	            .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with ID: " + dto.getInventoryId()));
	    if(dto.getItemName()!=null)
	    existing.setItemName(dto.getItemName());
	    if(dto.getQuantityAvailable()!=null)
	    existing.setQuantityAvailable(dto.getQuantityAvailable());
	    if(dto.getReorderThreshold()!=null)
	    existing.setReorderThreshold(dto.getReorderThreshold());
	    if(dto.getItemId()!=null)
	    existing.setItemId(dto.getItemId());
	    if(dto.getRestaurantId()!=null)
	    existing.setRestaurantId(dto.getRestaurantId());
	    if(dto.getCategoryId()!=null)
	    existing.setCategoryId(dto.getCategoryId());

	    Inventory updated = inventoryRepository.save(existing);
	    return convertEntityToDto(updated);
	}

	@Override
	public void deleteInventory(Long id) {
	    Inventory inventory = inventoryRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with ID: " + id));
	    inventoryRepository.delete(inventory);
	}
	public InventoryDto convertEntityToDto(Inventory in) {
		if(in==null) {
			return null;
		}
		return new InventoryDto(in.getInventoryId(),in.getItemName(),in.getQuantityAvailable(),in.getReorderThreshold(),in.getItemId(),in.getRestaurantId(),in.getCategoryId()); 
	}
	public Inventory convertDtoToEntity(InventoryDto dto) {
		if(dto==null) {
			return null;
		}
		return new Inventory(dto.getInventoryId(),dto.getItemName(),dto.getQuantityAvailable(),dto.getReorderThreshold(),dto.getItemId(),dto.getRestaurantId(),dto.getCategoryId());
	}

	@Override
	@Transactional
	public void updateInventory(InventoryUpdateRequest req) {
	 
	    // Loop through all order items (because one order may contain multiple items)
	    for (InventoryUpdateItem item : req.getItems()) {
	 
	        Inventory inv = inventoryRepository.findByItemId(item.getItemId())
	                .orElseThrow(() -> new RuntimeException("Inventory not found for item: " + item.getItemId()));
	 
	        // Reduce stock
	        int newQty = inv.getQuantityAvailable() - item.getQuantity();
	        inv.setQuantityAvailable(Math.max(newQty, 0));   // avoid negative stock
	 
	        // If out of stock: mark unavailable in MENU-SERVICE
	        if (newQty <= 0) {
	            menuclient.updateAvailability(item.getItemId(), false);
	        }
	 
	        inventoryRepository.save(inv);
	    }
	}

	@Override
	public List<InventoryDto> getAllInventory() {
		// TODO Auto-generated method stub
		List<Inventory> in=inventoryRepository.findAll();
		return in.stream().map(this::convertEntityToDto).collect(Collectors.toList());
	}

	@Override
	public List<InventoryDto> getInventoryByRestaurantId(Long restaurantId) {
		List<Inventory> in = inventoryRepository.findByRestaurantId(restaurantId);
		return in.stream().map(this::convertEntityToDto).collect(Collectors.toList());
	}

}


