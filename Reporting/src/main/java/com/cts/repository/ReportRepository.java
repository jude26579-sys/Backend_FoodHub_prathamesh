package com.cts.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.entities.OverallReport;

@Repository
public interface ReportRepository extends JpaRepository<OverallReport, Long> {
	OverallReport findByReportId(String reportId);

	List<OverallReport> findByReportIdEndingWith(String lastThree);

	List<OverallReport> findByReportGeneratedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
