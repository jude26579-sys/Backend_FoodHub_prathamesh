package com.cts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="MENU-SERVICE",configuration = FeignClientInterceptor.class)
public interface MenuClient {
	@GetMapping("/api/menu/search")
    MenuResponse getMenuItem(
            @RequestParam(required = false) Long restaurantId,
            @RequestParam(required = false) String restaurantName,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) String itemName);

	
	 @PutMapping("/api/menu/{itemId}/availability")
	    void updateAvailability(@PathVariable Long itemId,
	                            @RequestParam boolean isavailable);

}