package com.cts.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cts.clients.ReportingClient;
import com.cts.clients.RestaurantClient;
import com.cts.dtos.OrderReportEntryDto;
import com.cts.dtos.OverallReportDto;
import com.cts.dtos.RestaurantReportDto;
import com.cts.entities.Duration;
import com.cts.entities.OrderReportEntry;
import com.cts.entities.OverallReport;
import com.cts.entities.ReportRequest;
import com.cts.entities.RestaurantReport;
import com.cts.repository.ReportRepository;

import jakarta.transaction.Transactional;

@Service
public class ReportingServiceImpl implements ReportingService {
	private static final Logger logger = LoggerFactory.getLogger(ReportingServiceImpl.class);
	
	private ReportRepository reportRepository;
	private ReportingClient reportingClient;
	private RestaurantClient restaurantClient;
	
	public ReportingServiceImpl(ReportRepository reportRepository, ReportingClient reportingClient, RestaurantClient restaurantClient) {
		super();
		this.reportRepository = reportRepository;
		this.reportingClient = reportingClient;
		this.restaurantClient = restaurantClient;
	}

	@Override
	public List<OverallReportDto> getAllReports() {
	    List<OverallReport> reports = reportRepository.findAll();

	    return reports.stream().map(report -> {
	        OverallReportDto dto = new OverallReportDto();
	        dto.setReportId(report.getReportId());
	        dto.setDuration(report.getDuration());
	        dto.setTotalSales(report.getTotalSales());
	        dto.setRestaurantCount(report.getRestaurantCount());
	        dto.setOrderCount(report.getOrderCount());
	        dto.setReportGeneratedAt(report.getReportGeneratedAt());

	        List<RestaurantReportDto> restaurantDtos = report.getRestaurants().stream().map(restaurant -> {
	            List<OrderReportEntryDto> orderDtos = restaurant.getOrders().stream().map(order -> {
	                OrderReportEntryDto orderDto = new OrderReportEntryDto();
	                orderDto.setOrderId(order.getOrderId());
	                orderDto.setOrderSalesAmt(order.getOrderSalesAmt());
	                orderDto.setOrderStatus(order.getOrderStatus());
	                orderDto.setCreatedAt(order.getCreatedAt());
	                return orderDto;
	            }).toList();

	            RestaurantReportDto restaurantDto = new RestaurantReportDto();
	            restaurantDto.setRestaurantId(restaurant.getRestaurantId());
	            restaurantDto.setRestaurantOrderCount(restaurant.getRestaurantOrderCount());
	            restaurantDto.setOrders(orderDtos);
	            return restaurantDto;
	        }).toList();

	        dto.setRestaurants(restaurantDtos);
	        return dto;
	    }).toList();
	}

	@Override
	public OverallReportDto getReportById(String reportId) {
	    OverallReport report = reportRepository.findByReportId(reportId);

	    if (report == null) {
	        return null; // or throw a custom NotFoundException
	    }

	    OverallReportDto dto = new OverallReportDto();
	    dto.setReportId(report.getReportId());
	    dto.setDuration(report.getDuration());
	    dto.setTotalSales(report.getTotalSales());
	    dto.setRestaurantCount(report.getRestaurantCount());
	    dto.setOrderCount(report.getOrderCount());
	    dto.setReportGeneratedAt(report.getReportGeneratedAt());

	    List<RestaurantReportDto> restaurantDtos = report.getRestaurants().stream().map(restaurant -> {
	        List<OrderReportEntryDto> orderDtos = restaurant.getOrders().stream().map(order -> {
	            OrderReportEntryDto orderDto = new OrderReportEntryDto();
	            orderDto.setOrderId(order.getOrderId());
	            orderDto.setOrderSalesAmt(order.getOrderSalesAmt());
	            orderDto.setOrderStatus(order.getOrderStatus());
	            orderDto.setCreatedAt(order.getCreatedAt());
	            return orderDto;
	        }).toList();

	        RestaurantReportDto restaurantDto = new RestaurantReportDto();
	        restaurantDto.setRestaurantId(restaurant.getRestaurantId());
	        restaurantDto.setRestaurantOrderCount(restaurant.getRestaurantOrderCount());
	        restaurantDto.setOrders(orderDtos);
	        return restaurantDto;
	    }).toList();

	    dto.setRestaurants(restaurantDtos);
	    return dto;
	}

	@Override
	public OverallReportDto addReport(ReportRequest reportRequest) {
	    logger.info("\n\n");
	    logger.info("╔════════════════════════════════════════════════════════════════╗");
	    logger.info("║          🚀 REPORT GENERATION SERVICE STARTED 🚀              ║");
	    logger.info("╚════════════════════════════════════════════════════════════════╝");
	    
	    try {
	        // Log the incoming request
	        logger.info("📊 Report Generation Request Received:");
	        logger.info("   Start Date: {}", reportRequest.getStartDate());
	        logger.info("   End Date: {}", reportRequest.getEndDate());
	        logger.info("   Restaurant IDs: {}", reportRequest.getRestaurantIds());
	        
	        // Validate input
	        if (reportRequest.getStartDate() == null || reportRequest.getEndDate() == null) {
	            throw new IllegalArgumentException("Start and end dates are required");
	        }
	        if (reportRequest.getRestaurantIds() == null || reportRequest.getRestaurantIds().isEmpty()) {
	            throw new IllegalArgumentException("At least one restaurant ID is required");
	        }
	        
	        logger.info("\n📡 Calling PlacingOrder Service via Feign Client (http://localhost:8083/api/orders)...");
	        logger.info("   This will retrieve all orders from the database");
	        
	        List<com.cts.dtos.OrdersDto> allOrders = reportingClient.getAllOrders();
	        
	        logger.info("✅ SUCCESS! Received {} total orders from PlacingOrder service", allOrders.size());
	        
	        if (allOrders.isEmpty()) {
	            logger.warn("⚠️ WARNING: No orders found! PlacingOrder service returned empty list");
	        } else {
	            logger.info("📋 First few orders from PlacingOrder:");
	            for (int i = 0; i < Math.min(3, allOrders.size()); i++) {
	                com.cts.dtos.OrdersDto order = allOrders.get(i);
	                logger.info("   [{}] Order ID: {} | Restaurant: {} | Amount: ₹{} | Status: {}",
	                    (i+1), order.getOrderId(), order.getRestaurantId(), order.getSubTotal(), order.getOrderStatus());
	            }
	        }

	        Map<Long, List<OrderReportEntry>> ordersByRestaurant = new HashMap<>();
	        double totalSales = 0;
	        int totalOrders = 0;
	        int processedCount = 0;
	        int skippedCount = 0;

	        for (com.cts.dtos.OrdersDto order : allOrders) {
	            try {
	                // Extract order ID
	                Long orderId = order.getOrderId();
	                if (orderId == null) {
	                    logger.debug("❌ Order has null orderId, skipping");
	                    skippedCount++;
	                    continue;
	                }
	                
	                // Extract restaurant ID and handle Integer/Long conversion
	                Integer restaurantIdInt = order.getRestaurantId();
	                if (restaurantIdInt == null) {
	                    logger.debug("❌ Order {} has null restaurantId, skipping", orderId);
	                    skippedCount++;
	                    continue;
	                }
	                Long restaurantId = restaurantIdInt.longValue();

	                // Check if this order's restaurant is in the selected list
	                boolean restaurantMatches = false;
	                if (restaurantId != null) {
	                    for (Long selectedRestId : reportRequest.getRestaurantIds()) {
	                        if (restaurantId.equals(selectedRestId)) {
	                            restaurantMatches = true;
	                            break;
	                        }
	                    }
	                }
	                
	                if (!restaurantMatches) {
	                    logger.debug("⏭️ Order {} - restaurant {} not selected", orderId, restaurantId);
	                    skippedCount++;
	                    continue;
	                }
	                
	                // Extract createdAt
	                LocalDateTime createdAt = order.getCreatedAt();
	                if (createdAt == null) {
	                    logger.warn("⚠️ Order {} has null createdAt, using current time", orderId);
	                    createdAt = LocalDateTime.now();
	                }
	                
	                // Check if date is within range
	                LocalDate orderDate = createdAt.toLocalDate();
	                LocalDate startDate = reportRequest.getStartDate();
	                LocalDate endDate = reportRequest.getEndDate();
	                
	                if (orderDate.isBefore(startDate) || orderDate.isAfter(endDate)) {
	                    logger.debug("⏭️ Order {} is outside date range: {} (required: {} to {})", 
	                        orderId, orderDate, startDate, endDate);
	                    skippedCount++;
	                    continue;
	                }
	                
	                // Extract order status
	                String status = order.getOrderStatus() != null
	                    ? order.getOrderStatus().toString()
	                    : "UNKNOWN";
	                
	                // Extract subtotal
	                Double subTotal = order.getSubTotal() != null ? order.getSubTotal() : 0.0;
	                
	                logger.info("📦 Processing Order {} | Restaurant: {} | Status: {} | Date: {} | Amount: ₹{}",
	                    orderId, restaurantId, status, orderDate, subTotal);

	                // Create report entry
	                OrderReportEntry entry = new OrderReportEntry();
	                entry.setOrderId(orderId);
	                entry.setOrderSalesAmt(subTotal);
	                entry.setOrderStatus(status);
	                entry.setCreatedAt(createdAt);

	                // Add to map
	                ordersByRestaurant.computeIfAbsent(restaurantId, k -> new ArrayList<>()).add(entry);
	                totalSales += subTotal;
	                totalOrders++;
	                processedCount++;
	                
	                logger.info("   ✅ Added to report | Running Total: ₹{}", totalSales);
	                
	            } catch (Exception e) {
	                logger.warn("⚠️ Skipping malformed order: {}", order);
	                logger.warn("   Error: {}", e.getMessage());
	                skippedCount++;
	                continue;
	            }
	        }

	        logger.info("═══════════════════════════════════════════════════════════════");
	        logger.info("📈 Processing Summary:");
	        logger.info("   Total Orders Fetched: {}", allOrders.size());
	        logger.info("   Orders Processed: {}", processedCount);
	        logger.info("   Orders Skipped: {}", skippedCount);
	        logger.info("   Final Total Orders: {}", totalOrders);
	        logger.info("   Final Total Sales: ₹{}", totalSales);
	        logger.info("═══════════════════════════════════════════════════════════════");

	        // Create overall report
	        OverallReport overallReport = new OverallReport();
	        String reportId = "REP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	        overallReport.setReportId(reportId);
	        overallReport.setDuration(new Duration(reportRequest.getStartDate(), reportRequest.getEndDate()));
	        overallReport.setTotalSales(totalSales);
	        overallReport.setRestaurantCount(reportRequest.getRestaurantIds().size());
	        overallReport.setOrderCount(totalOrders);
	        overallReport.setReportGeneratedAt(LocalDateTime.now());

	        // Create restaurant-specific reports
	        List<RestaurantReport> restaurantReports = new ArrayList<>();
	        for (Long restaurantId : reportRequest.getRestaurantIds()) {
	            List<OrderReportEntry> orders = ordersByRestaurant.getOrDefault(restaurantId, new ArrayList<>());

	            RestaurantReport restaurantReport = new RestaurantReport();
	            restaurantReport.setRestaurantId(restaurantId);
	            restaurantReport.setRestaurantOrderCount(orders.size());
	            restaurantReport.setOverallReport(overallReport);

	            // Link orders to report
	            for (OrderReportEntry order : orders) {
	                order.setRestaurantReport(restaurantReport);
	            }

	            restaurantReport.setOrders(orders);
	            restaurantReports.add(restaurantReport);
	        }

	        overallReport.setRestaurants(restaurantReports);
	        
	        // Save to database
	        OverallReport savedReport = reportRepository.save(overallReport);
	        logger.info("✅ Report saved with ID: {}", reportId);

	        // Convert to DTO
	        OverallReportDto reportDto = new OverallReportDto();
	        reportDto.setReportId(savedReport.getReportId());
	        reportDto.setDuration(savedReport.getDuration());
	        reportDto.setTotalSales(savedReport.getTotalSales());
	        reportDto.setRestaurantCount(savedReport.getRestaurantCount());
	        reportDto.setOrderCount(savedReport.getOrderCount());
	        reportDto.setReportGeneratedAt(savedReport.getReportGeneratedAt());

	        // Convert restaurant reports to DTOs
	        List<RestaurantReportDto> restaurantDtos = savedReport.getRestaurants().stream().map(restaurant -> {
	            List<OrderReportEntryDto> orderDtos = restaurant.getOrders().stream().map(order -> {
	                OrderReportEntryDto dto = new OrderReportEntryDto();
	                dto.setOrderId(order.getOrderId());
	                dto.setOrderSalesAmt(order.getOrderSalesAmt());
	                dto.setOrderStatus(order.getOrderStatus());
	                dto.setCreatedAt(order.getCreatedAt());
	                return dto;
	            }).toList();

	            RestaurantReportDto restaurantDto = new RestaurantReportDto();
	            restaurantDto.setRestaurantId(restaurant.getRestaurantId());
	            restaurantDto.setRestaurantOrderCount(restaurant.getRestaurantOrderCount());
	            restaurantDto.setOrders(orderDtos);
	            return restaurantDto;
	        }).toList();

	        reportDto.setRestaurants(restaurantDtos);
	        
	        logger.info("✅ Report generation completed successfully");
	        return reportDto;
	        
	    } catch (Exception e) {
	        logger.error("❌ Error generating report: {}", e.getMessage(), e);
	        throw new RuntimeException("Failed to generate report: " + e.getMessage(), e);
	    }
	}
	
	@Override
	@Transactional
	public void deleteReportById(String reportId) {
	    if (reportId == null || reportId.isBlank()) {
	        throw new IllegalArgumentException("reportId must not be null or blank.");
	    }

	    OverallReport report = reportRepository.findByReportId(reportId);
	    if (report != null) {
	        reportRepository.delete(report);
	    } 
	}

	@Override
	public List<RestaurantClient.RestaurantDto> getAllRestaurants() {
		System.out.println("� ═══════════════════════════════════════════════════════");
		System.out.println("🔍 Fetching all restaurants from Restaurant Service");
		System.out.println("🔍 URL: http://localhost:8182/api/restaurants");
		System.out.println("🔍 ═══════════════════════════════════════════════════════");
		try {
			System.out.println("📡 Making Feign call to RestaurantClient.getAllRestaurants()...");
			List<RestaurantClient.RestaurantDto> restaurants = restaurantClient.getAllRestaurants();
			
			System.out.println("✅ Successfully fetched " + restaurants.size() + " restaurants");
			
			if (restaurants.isEmpty()) {
				System.out.println("⚠️  WARNING: Restaurant list is empty!");
				System.out.println("    Possible causes:");
				System.out.println("    1. Restaurant Service is not running on port 8182");
				System.out.println("    2. No restaurants are registered in the database");
				System.out.println("    3. Authentication token is invalid/missing");
			} else {
				restaurants.forEach(r -> {
					System.out.println("   ✓ ID: " + r.getRestaurantId() 
						+ " | Name: " + r.getRestaurantName() 
						+ " | Status: " + r.getStatus());
				});
			}
			return restaurants;
		} catch (Exception e) {
			System.out.println("❌ Error fetching restaurants: " + e.getMessage());
			System.out.println("   Error Type: " + e.getClass().getSimpleName());
			System.out.println("   Cause: " + e.getCause());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

//	@Override
//	public List<OverallReportDto> searchReportsByDateRange(LocalDate start, LocalDate end) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
