package com.teamwork.telegrambotanimalshelter.repository;

import com.teamwork.telegrambotanimalshelter.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByTrialPeriodId(Long id);
    List<Report> findAllByDateOfReportAndTrialPeriodId(LocalDate dateOfReport, Long id);
}
