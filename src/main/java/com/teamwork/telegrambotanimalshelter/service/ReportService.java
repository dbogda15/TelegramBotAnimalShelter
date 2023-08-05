package com.teamwork.telegrambotanimalshelter.service;

import com.teamwork.telegrambotanimalshelter.model.report.Report;

import java.util.List;

public interface ReportService {
    /**
     * Создание и добавление в БД нового отчёта
     * @param report
     *
     */
    Report create(Report report);

    /**
     * Получение отчёта по его ID
     * @param id
     *
     */
    Report getById(Long id);

    /**
     * Получение списка всех отчетов
     *
     */
    List<Report> getAll();

    /**
     * Удалить отчет из БД по его ID
     * @param reportId - уникальный ID из БД
     */
    void deleteById(Long reportId);
    /**
     * Оьновить данные в существующем отчёте
     * @param report
     */
    Report update(Report report);
}
