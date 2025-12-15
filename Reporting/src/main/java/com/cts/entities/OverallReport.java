package com.cts.entities;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "reporting")
public class OverallReport {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Column(unique = true, nullable = false)
    private String reportId;

    @Embedded
    private Duration duration;

    @OneToMany(mappedBy = "overallReport", cascade = CascadeType.ALL)
    private List<RestaurantReport> restaurants;

    private Double totalSales;
    private Integer restaurantCount;
    private Integer orderCount;
    private LocalDateTime reportGeneratedAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public Duration getDuration() {
		return duration;
	}
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	public List<RestaurantReport> getRestaurants() {
		return restaurants;
	}
	public void setRestaurants(List<RestaurantReport> restaurants) {
		this.restaurants = restaurants;
	}
	public Double getTotalSales() {
		return totalSales;
	}
	public void setTotalSales(Double totalSales) {
		this.totalSales = totalSales;
	}
	public Integer getRestaurantCount() {
		return restaurantCount;
	}
	public void setRestaurantCount(Integer restaurantCount) {
		this.restaurantCount = restaurantCount;
	}
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	public LocalDateTime getReportGeneratedAt() {
		return reportGeneratedAt;
	}
	public void setReportGeneratedAt(LocalDateTime reportGeneratedAt) {
		this.reportGeneratedAt = reportGeneratedAt;
	}

    
}
