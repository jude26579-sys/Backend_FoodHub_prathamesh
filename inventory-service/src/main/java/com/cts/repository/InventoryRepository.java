package com.cts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {

	Optional<Inventory> findByItemIdAndRestaurantIdAndCategoryId(Long itemId, Long restaurantId, Long categoryId);
	Boolean existsByRestaurantIdAndCategoryIdAndItemId(Long restaurantId, Long categoryId, Long itemId);
	Optional<Inventory> findByItemId(Long itemId);
	Inventory findByItemIdAndRestaurantId(Long itemId, Long restaurantId);
	List<Inventory> findByRestaurantId(Long restaurantId);
}
