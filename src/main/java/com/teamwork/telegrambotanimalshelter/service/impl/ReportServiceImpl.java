package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.exceptions.IncorrectArgumentException;
import com.teamwork.telegrambotanimalshelter.exceptions.ObjectAlreadyExistsException;
import com.teamwork.telegrambotanimalshelter.model.Report;
import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.enums.TrialPeriodType;
import com.teamwork.telegrambotanimalshelter.model.Report;
import com.teamwork.telegrambotanimalshelter.repository.ReportRepository;
import com.teamwork.telegrambotanimalshelter.service.ReportService;
import com.teamwork.telegrambotanimalshelter.service.TrialPeriodService;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final TrialPeriodService trialPeriodService;

    public ReportServiceImpl(ReportRepository reportRepository, TrialPeriodService trialPeriodService) {
        this.reportRepository = reportRepository;
        this.trialPeriodService = trialPeriodService;
    }

    @Override
    public Report create(Report report) {
        Long ownerId = trialPeriodService.findById(report.getTrialPeriodId()).getOwnerId();
        TrialPeriod trialPeriod = trialPeriodService.findAllByOwnerId(ownerId).stream()
                .findFirst().get();
        if (trialPeriod.getLastDateOfReport().isEqual(LocalDate.now())) {
            throw new ObjectAlreadyExistsException("Сегодня вы уже отправляли отчёт! Спасибо");
        }
        trialPeriodService.findById(report.getTrialPeriodId()).setLastDateOfReport(LocalDate.now());
        return reportRepository.save(report);
    }

    @Override
    public Report createFromTelegram(String photoId, String message, Long id) {
        TrialPeriod trialPeriod = trialPeriodService.findAllByOwnerId(id).stream()
                .filter(trialPeriod1 -> trialPeriod1.getPeriodType().equals(TrialPeriodType.IN_PROGRESS))
                .findFirst().get();

        if (trialPeriod.getLastDateOfReport().isEqual(LocalDate.now())) {
            throw new ObjectAlreadyExistsException("Сегодня вы уже отправляли отчёт! Спасибо");
        }

        List<String> messagesParts = split(message);
        Report report = create(new Report(photoId, messagesParts.get(0), messagesParts.get(1),
                messagesParts.get(2), LocalDate.now(), trialPeriod.getId()));
        trialPeriod.setLastDateOfReport(LocalDate.now());
        trialPeriodService.update(trialPeriod);
        return report;
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
    public List<Report> findAllByTrialPeriodId(Long id) {
        List<Report> reports = reportRepository.findAllByTrialPeriodId(id);
        if (reports.isEmpty()){
            throw new NotFoundException("Отчёты не найдены");
        }
        return reports;
    }

    @Override
    public List<Report> findAllByDateOfReportAndTrialPeriodId(LocalDate date, Long id) {
        List<Report> reports = reportRepository.findAllByDateOfReportAndTrialPeriodId(date, id);
        if (reports.isEmpty()){
            throw new NotFoundException("Отчёты не найдены");
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
    private List<String> split(String message) {
        Pattern pattern = Pattern.compile("(Рацион питания:)\\s(\\W+);\\n(Общее самочувствие:)\\s(\\W+);\\n(Изменение поведения:)\\s(\\W+);");
        if (message == null || message.isBlank() || !message.contains("Рацион питания:")
                || !message.contains("Общее самочувствие:") || !message.contains("Изменение поведения:")) {
            throw new IncorrectArgumentException("Описание не должно быть пустым и должно быть написано по шаблону с ключевыми словами!");
        }
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return new ArrayList<>(List.of(matcher.group(2), matcher.group(4), matcher.group(6)));
        } else {
            throw new IncorrectArgumentException("Проверьте правильность введённых данных и отправьте отчёт ещё раз.");
        }
    }
}
