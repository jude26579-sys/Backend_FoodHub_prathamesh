package com.cts.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.dto.InventoryDto;
import com.cts.dto.InventoryUpdateRequest;
import com.cts.service.InventoryService;
import com.cts.validation.Validation.Create;
import com.cts.validation.Validation.Update;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	@Autowired
public InventoryService inventoryservice;
	@PreAuthorize("hasAnyRole('VENDOR','CUSTOMER')")
	@GetMapping
	public ResponseEntity<List<InventoryDto>> getInventory(){
		List<InventoryDto> in=inventoryservice.getAllInventory();
		return ResponseEntity.ok(in);
	}
	
	@PreAuthorize("hasAnyRole('VENDOR','CUSTOMER')")
	@GetMapping("/restaurant/{restaurantId}")
	public ResponseEntity<List<InventoryDto>> getInventoryByRestaurant(@PathVariable Long restaurantId){
		List<InventoryDto> in=inventoryservice.getInventoryByRestaurantId(restaurantId);
		return ResponseEntity.ok(in);
	}
	
	@PreAuthorize("hasAnyRole('VENDOR','CUSTOMER')")
	@GetMapping("/{id}")
	public ResponseEntity<InventoryDto> getInventoryById(@PathVariable Long id){
		InventoryDto inventory=inventoryservice.getIventoryById(id);
		return ResponseEntity.ok(inventory);
	}
	@PreAuthorize("hasAnyRole('VENDOR','CUSTOMER')")
	@PostMapping
	public ResponseEntity<InventoryDto> create( @Validated(Create.class) @RequestBody InventoryDto dto){
		InventoryDto created=inventoryservice.addInventory(dto);
		return new ResponseEntity<InventoryDto>(created,HttpStatus.CREATED);	
	}
	@PreAuthorize("hasAnyRole('VENDOR','CUSTOMER')")
@PutMapping("/{id}")
    public ResponseEntity<InventoryDto> updateInventory(@PathVariable Long id,@Validated(Update.class)@RequestBody InventoryDto dto) {
		dto.setInventoryId(id);
        InventoryDto updated = inventoryservice.updateInventory(id,dto);
        return ResponseEntity.ok(updated);
    }
	@PreAuthorize("hasAnyRole('VENDOR','CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long id) {
        inventoryservice.deleteInventory(id);
        return ResponseEntity.ok("Inventory deleted successfully");
    }
	
	@PreAuthorize("hasAnyRole('VENDOR','CUSTOMER')")
	 @PutMapping("/update")
	    public ResponseEntity<Void> updateInventory(@RequestBody InventoryUpdateRequest request) {
	        inventoryservice.updateInventory(request);
	        return ResponseEntity.ok().build();
	    }
}




