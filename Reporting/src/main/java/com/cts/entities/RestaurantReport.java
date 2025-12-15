package com.cts.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class RestaurantReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long restaurantId;
    private Integer restaurantOrderCount;

    @OneToMany(mappedBy = "restaurantReport", cascade = CascadeType.ALL)
    private List<OrderReportEntry> orders;

    @ManyToOne
    @JoinColumn(name = "overall_report_id")
    private OverallReport overallReport;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Integer getRestaurantOrderCount() {
		return restaurantOrderCount;
	}

	public void setRestaurantOrderCount(Integer restaurantOrderCount) {
		this.restaurantOrderCount = restaurantOrderCount;
	}

	public List<OrderReportEntry> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderReportEntry> orders) {
		this.orders = orders;
	}

	public OverallReport getOverallReport() {
		return overallReport;
	}

	public void setOverallReport(OverallReport overallReport) {
		this.overallReport = overallReport;
	}

    
}