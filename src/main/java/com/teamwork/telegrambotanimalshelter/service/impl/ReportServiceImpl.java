package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.model.Report;
import com.teamwork.telegrambotanimalshelter.repository.ReportRepository;
import com.teamwork.telegrambotanimalshelter.service.ReportService;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Report create(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public Report getById(Long id) {
        Optional<Report> report = reportRepository.findById(id);
        if (report.isEmpty()){
            throw new NotFoundException("Отчёт с таким ID не найден");
        }
        return report.get();
    }

    @Override
    public List<Report> getAll() {
        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()){
            throw new NotFoundException("Отчёты не найден");
        }
        return reports;
    }

    @Override
    public void deleteById(Long id) {
        reportRepository.delete(getById(id));
    }

    @Override
    public Report update(Report report) {
       return reportRepository.save(report);
    }
}
