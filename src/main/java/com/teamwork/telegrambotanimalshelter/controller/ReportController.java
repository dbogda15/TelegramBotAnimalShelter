package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.Report;
import com.teamwork.telegrambotanimalshelter.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reports")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно"),
        @ApiResponse(responseCode = "400", description = "Ошибка в параметрах запроса"),
        @ApiResponse(responseCode = "404", description = "Несуществующий URL"),
        @ApiResponse(responseCode = "500", description = "Ошибка со стороны сервера")
})
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @Operation(summary = "Создание отчёта")
    ResponseEntity<Report> create(@RequestParam String photoId,
                                  @RequestParam String foodRation,
                                  @RequestParam String stateOfHealth,
                                  @RequestParam String behaviorChange,
                                  @RequestParam Long trialPeriodId) {
        Report report = reportService.create(new Report(photoId, foodRation, stateOfHealth, behaviorChange, LocalDate.now(), trialPeriodId));
        return ResponseEntity.ok(report);
    }

    @GetMapping
    @Operation(summary = "Получение всех отчётов")
    List<Report> getAll(){
        return reportService.getAll();
    }

    @GetMapping("/report_id")
    @Operation(summary = "Найти отчет по его ID")
    ResponseEntity<Report> findByReportId(@RequestParam Long id){
        Report report = reportService.getById(id);
        if (report == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }

    @GetMapping("/owner_id")
    @Operation(summary = "Найти отчет по ID испытательного срока")
    List<Report> getAllByOwnerId(@RequestParam Long id){
        return reportService.findAllByTrialPeriodId(id);
    }

    @GetMapping("/date_and_period_id")
    @Operation(summary = "Найти отчёты по ID испытательного срока и дате отправки")
    List<Report> getAllByTrialPeriodIdAndReportDate(@RequestParam @Parameter(example = "DD.MM.YYYY") LocalDate localDate,
                                                    @RequestParam Long id) {
        return reportService.findAllByDateOfReportAndTrialPeriodId(localDate, id);
    }

    @PutMapping
    @Operation(summary = "Обновление информации по отчёту")
    Report update(@RequestParam Long id,
                  @RequestParam (required = false) String photoId,
                  @RequestParam (required = false) String foodRation,
                  @RequestParam (required = false) String stateOfHealth,
                  @RequestParam (required = false) String behaviorChange,
                  @RequestParam (required = false) Long trialPeriodId,
                  @RequestParam (required = false) @Parameter(example = "DD.MM.YYYY") LocalDate dateOfReport) {
        Report report = reportService.getById(id);
        if (photoId != null){
            report.setPhotoId(photoId);
        }
        if (foodRation != null){
            report.setFoodRation(foodRation);
        }
        if(stateOfHealth != null){
            report.setStateOfHealth(stateOfHealth);
        }
        if(behaviorChange != null){
            report.setBehaviorChange(behaviorChange);
        }
        if(trialPeriodId != null){
            report.setTrialPeriodId(trialPeriodId);
        }
        if (dateOfReport != null){
            report.setDateOfReport(dateOfReport);
        }
        return reportService.update(report);
    }

    @DeleteMapping
    @Operation(summary = "Удаление отчёта из БД")
    void delete(@RequestParam Long id) {
        reportService.deleteById(id);
        System.out.println("Отчёт был удален");
    }
}
