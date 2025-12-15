package com.cts.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.dto.LoginRequest;
import com.cts.dto.RegisterRequest;
import com.cts.dto.VendorResponse;
import com.cts.entity.Vendor;
import com.cts.services.VendorService;

@RestController
@RequestMapping("/vendor")
public class VendorController {
	
	@Autowired
	private VendorService vendorService;
	
	@PostMapping("/register")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VendorResponse> registerVendor(@RequestBody RegisterRequest request) {
		VendorResponse vendor=vendorService.register(request.getName(), request.getEmail(), request.getPassword());
		
		return new ResponseEntity<>(vendor,HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<VendorResponse> loginVendor(@RequestBody LoginRequest request) {
		Vendor vendor = vendorService.login(request.getEmail(), request.getPassword());
		
		VendorResponse response = new VendorResponse();
		response.setId(vendor.getId());
		response.setName(vendor.getName());
		response.setEmail(vendor.getEmail());
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{vendorId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('VENDOR')")
	public ResponseEntity<?> getVendorById(@PathVariable Long vendorId) {
		
		Vendor vendor=vendorService.getVendorById(vendorId);
		if(vendor==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vendor not found");
		}
		VendorResponse response=new VendorResponse();
		response.setId(vendor.getId());
		response.setName(vendor.getName());
		response.setEmail(vendor.getEmail());
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllVendors() {
		java.util.List<Vendor> vendors = vendorService.getAllVendors();
		java.util.List<VendorResponse> responses = new java.util.ArrayList<>();
		
		for (Vendor vendor : vendors) {
			VendorResponse response = new VendorResponse();
			response.setId(vendor.getId());
			response.setName(vendor.getName());
			response.setEmail(vendor.getEmail());
			responses.add(response);
		}
		
		return ResponseEntity.ok(responses);
	}
	
	@PutMapping("/{vendorId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateVendor(@PathVariable Long vendorId, @RequestBody RegisterRequest request) {
		try {
			Vendor vendor = vendorService.getVendorById(vendorId);
			if (vendor == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vendor not found");
			}
			
			vendor.setName(request.getName());
			vendor.setEmail(request.getEmail());
			
			// Update password only if provided
			if (request.getPassword() != null && !request.getPassword().isEmpty()) {
				vendor.setPassword(request.getPassword());
			}
			
			vendorService.updateVendor(vendor);
			
			VendorResponse response = new VendorResponse();
			response.setId(vendor.getId());
			response.setName(vendor.getName());
			response.setEmail(vendor.getEmail());
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating vendor: " + e.getMessage());
		}
	}
	
	@DeleteMapping("/{vendorId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteVendor(@PathVariable Long vendorId) {
		try {
			Vendor vendor = vendorService.getVendorById(vendorId);
			if (vendor == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vendor not found");
			}
			
			vendorService.deleteVendor(vendorId);
			
			return ResponseEntity.ok("Vendor deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting vendor: " + e.getMessage());
		}
	}
	
	
	
}