package com.cts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.client.InventoryClient;
import com.cts.dto.OrdersDto;
import com.cts.dto.RestaurantRequest;
import com.cts.dto.RestaurantResponse;
import com.cts.service.RestaurantService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/restaurants") 
public class RestaurantController {

    @Autowired
    private RestaurantService service;
    @Autowired
    private InventoryClient inventoryClient;

  
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RestaurantResponse> addRestaurant(@Valid @RequestBody RestaurantRequest request) {
        RestaurantResponse response = service.addRestaurant(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable Long id,
            @Valid @RequestBody RestaurantRequest request) {
        return ResponseEntity.ok(service.updateRestaurant(id, request));
    }
    

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        service.deleteRestaurant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PreAuthorize("hasAnyRole('ADMIN','VENDOR','CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> viewRestaurants() {
        return ResponseEntity.ok(service.getAllRestaurants());
    }


    @PreAuthorize("hasAnyRole('ADMIN','VENDOR','CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getRestaurantById(id));
    }



    @PreAuthorize("hasAnyRole('ADMIN','VENDOR','CUSTOMER')")
    @GetMapping("/name/{restaurantName}")
    public ResponseEntity<RestaurantResponse> getRestaurantByName(@PathVariable String restaurantName) {
        return ResponseEntity.ok(service.getRestaurantByName(restaurantName));
    }
  

    @PreAuthorize("hasAnyRole('ADMIN','VENDOR','CUSTOMER')")
    @GetMapping("/my")
    public ResponseEntity<List<RestaurantResponse>> getMyRestaurants(Authentication authentication) {
        Long vendorId=extractVendorIdFromJwt(authentication);
        List<RestaurantResponse> restaurants=service.getRestaurantsByVendor(vendorId);     
        return ResponseEntity.ok(restaurants);
    }

    @PreAuthorize("hasAnyRole('ADMIN','VENDOR')")
    @PutMapping("/{id}/status")
    public ResponseEntity<RestaurantResponse> updateRestaurantStatus(
            @PathVariable Long id,
            @RequestBody RestaurantRequest request,
            Authentication authentication) {
        // Extract vendor ID from JWT
        Long vendorId = extractVendorIdFromJwt(authentication);
        
        // Check if the restaurant belongs to this vendor (or if user is admin)
        RestaurantResponse restaurant = service.getRestaurantById(id);
        if (!restaurant.getVendorId().equals(vendorId) && !isAdmin(authentication)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        // Update only the status
        return ResponseEntity.ok(service.updateRestaurantStatus(id, request.getStatus()));
    }

    // Helper method to check if user is admin
    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

@PostMapping("/process-order")
    public ResponseEntity<String> processOrder(@RequestBody OrdersDto order) {
        service.processOrder(order);
        return ResponseEntity.ok("Order processed and inventory updated");
    }

  
  private Long extractVendorIdFromJwt(Authentication authentication) {
	  var jwt=(org.springframework.security.oauth2.jwt.Jwt) authentication.getPrincipal();
	  Object idClaim = jwt.getClaim("id");
	  if (idClaim instanceof Long) {
	      return (Long) idClaim;
	  } else if (idClaim instanceof Integer) {
	      return ((Integer) idClaim).longValue();
	  } else if (idClaim instanceof String) {
	      return Long.valueOf((String) idClaim);
	  } else {
	      throw new IllegalArgumentException("Invalid id claim type: " + (idClaim != null ? idClaim.getClass() : "null"));
	  }
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
}