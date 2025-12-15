package com.cts.service;

import java.time.LocalDate;
import java.util.List;

import com.cts.clients.RestaurantClient;
import com.cts.dtos.OverallReportDto;
import com.cts.entities.ReportRequest;

public interface ReportingService {
	List<OverallReportDto> getAllReports();
	OverallReportDto getReportById(String reportId);
	OverallReportDto addReport(ReportRequest reportRequest);
	//List<OverallReportDto> searchReportsByDateRange(LocalDate start, LocalDate end);
	void deleteReportById(String reportId);
	
	/**
	 * Fetch all restaurants from Restaurant Service
	 * @return List of RestaurantDto objects
	 */
	List<RestaurantClient.RestaurantDto> getAllRestaurants();
}
