package com.teamwork.telegrambotanimalshelter.service;

import com.teamwork.telegrambotanimalshelter.model.Report;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    /**
     * Создание и добавление в БД нового отчёта
     * @param report
     *
     */
    Report create(Report report);

    /**
     * Создание отчета через сообщение из Телеграм
     *
     * @param photoId
     * @param message
     * @param id
     */
    void createFromTelegram(String photoId, String message, Long id);
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
     * Получение списка всех отчётов по ID испытательного срока
     * @param id
     * @return
     */
    List<Report> findAllByTrialPeriodId(Long id);

    /**
     *Получение списка отчётов по ID пробного периода и дате отправления
     * @param date
     * @param id
     * @return
     */
    List<Report> findAllByDateOfReportAndTrialPeriodId(LocalDate date, Long id);
    /**
     * Удалить отчет из БД по его ID
     * @param reportId - уникальный ID из БД
     */
    String deleteById(Long reportId);
    /**
     * Обновить данные в существующем отчёте
     * @param report
     */
    Report update(Report report);
}
