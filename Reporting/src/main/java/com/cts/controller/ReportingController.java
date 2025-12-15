package com.cts.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.clients.RestaurantClient;
import com.cts.dtos.OverallReportDto;
import com.cts.entities.ReportRequest;
import com.cts.service.ReportingService;

@RestController
@RequestMapping("/api/report")
public class ReportingController {
	private ReportingService reportingService;
	
	public ReportingController(ReportingService reportingService) {
		super();
		this.reportingService = reportingService;
	}
	
	/**
	 * GET /api/report/restaurants
	 * Fetch all restaurants from the Restaurant Service
	 * 
	 * @return List of RestaurantDto objects
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/restaurants")
	public ResponseEntity<List<RestaurantClient.RestaurantDto>> getRestaurants() {
		System.out.println("═══════════════════════════════════════════════════════════════");
		System.out.println("📋 GET /api/report/restaurants - Fetching all restaurants");
		System.out.println("═══════════════════════════════════════════════════════════════");
		try {
			List<RestaurantClient.RestaurantDto> restaurants = reportingService.getAllRestaurants();
			System.out.println("✅ Controller returning " + restaurants.size() + " restaurants");
			System.out.println("═══════════════════════════════════════════════════════════════");
			return ResponseEntity.ok(restaurants);
		} catch (Exception e) {
			System.out.println("❌ Error in controller: " + e.getMessage());
			e.printStackTrace();
			System.out.println("═══════════════════════════════════════════════════════════════");
			return ResponseEntity.status(500).build();
		}
	}
	
	/**
	 * POST /api/report
	 * Generate report for given date range and restaurant(s)
	 * 
	 * Request Body:
	 * {
	 *   "startDate": "2025-11-10",
	 *   "endDate": "2025-12-10",
	 *   "restaurantIds": [1, 2, 3]
	 * }
	 * 
	 * @param reportRequest - Contains startDate, endDate, and restaurantIds
	 * @return OverallReportDto with aggregated report data
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
    public ResponseEntity<OverallReportDto> generateReport(@RequestBody ReportRequest reportRequest) {
		System.out.println("📊 Report generation request received");
		System.out.println("   Start Date: " + reportRequest.getStartDate());
		System.out.println("   End Date: " + reportRequest.getEndDate());
		System.out.println("   Restaurant IDs: " + reportRequest.getRestaurantIds());
		
        OverallReportDto report = reportingService.addReport(reportRequest);
        
        System.out.println("✅ Report generated successfully");
        return ResponseEntity.ok(report);
    }

}
