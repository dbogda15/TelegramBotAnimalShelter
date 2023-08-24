package com.teamwork.telegrambotanimalshelter.service;

import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;

import java.util.List;

public interface TrialPeriodService {
    /**
     * Создание и сохранение испытательного срока в БД
     * @param trialPeriod
     * @return
     */
    TrialPeriod create(TrialPeriod trialPeriod);

    /**
     * Поиск испытательного срока по его ID
     * @param id
     * @return
     */
    TrialPeriod findById(Long id);

    /**
     * Удаление испытательного срока из БД
     * @param trialPeriod
     */
    void delete(TrialPeriod trialPeriod);

    /**
     * Получение всех испытательных сроков
     * @return
     */
    List<TrialPeriod> findAll();

    /**
     * Получение испытательных сроков владельца по его ID
     * @param id
     * @return
     */
    List<TrialPeriod> findAllByOwnerId(Long id);

    /**
     * Удаление испытательного срока по ID
     * @param id
     */
    void delete(Long id);

    /**
     * Обновление данных по испытательному сроку
     * @param trialPeriod
     * @return
     */
    TrialPeriod update(TrialPeriod trialPeriod);
}
