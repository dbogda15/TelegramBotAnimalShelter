package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.enums.TrialPeriodType;
import com.teamwork.telegrambotanimalshelter.repository.TrialPeriodRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrialPeriodServiceImplTest {

    @Mock
    TrialPeriodRepository trialPeriodRepository;

    @InjectMocks
    TrialPeriodServiceImpl out;

    final static Long OWNER_ID = 1L;
    final static Long ANIMAL_ID = 1L;
    final static Long PERIOD_ID = 1L;
    final static TrialPeriod TRIAL_PERIOD = new TrialPeriod(PERIOD_ID, LocalDate.now(), LocalDate.now().plusDays(30), LocalDate.now().minusDays(1),
            OWNER_ID, ANIMAL_ID, AnimalType.CAT, new ArrayList<>(), TrialPeriodType.IN_PROGRESS);
    @Test
    @DisplayName("Создание испытательного срока")
    void shouldReturnValidTrialPeriodWhenCreate() {
        when(trialPeriodRepository.save(TRIAL_PERIOD))
                .thenReturn(TRIAL_PERIOD);

        assertEquals(TRIAL_PERIOD, out.create(TRIAL_PERIOD));

        verify(trialPeriodRepository, times(1)).save(TRIAL_PERIOD);
    }

    @Test
    @DisplayName("Поиск испытательного срока по ID")
    void shouldReturnValidTrialPeriodWhenFindById() {
        when(trialPeriodRepository.findById(PERIOD_ID))
                .thenReturn(Optional.of(TRIAL_PERIOD));

        assertEquals(TRIAL_PERIOD, out.findById(PERIOD_ID));

        verify(trialPeriodRepository, times(1)).findById(PERIOD_ID);
    }

    @Test
    @DisplayName("Поиск всех испытательных сроков")
    void shouldReturnListOfAllTrialPeriodWhenFindAll() {
        when(trialPeriodRepository.findAll())
                .thenReturn(List.of(TRIAL_PERIOD));

        assertEquals(List.of(TRIAL_PERIOD), out.findAll());

        verify(trialPeriodRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Поиск испытательных сроков оп ID владельца")
    void shouldReturnCorrectListOfTrialPeriodWhenFindAllByOwnerId() {
        when(trialPeriodRepository.findAllByOwnerId(OWNER_ID))
                .thenReturn(List.of(TRIAL_PERIOD));

        assertEquals(List.of(TRIAL_PERIOD), out.findAllByOwnerId(OWNER_ID));

        verify(trialPeriodRepository, times(1)).findAllByOwnerId(OWNER_ID);
    }

    @Test
    @DisplayName("Получение исключения, когда результат поиска - пустой лист (findById)")
    void shouldThrowNotFoundExceptionWhenFindById() {
        when(trialPeriodRepository.findById(PERIOD_ID))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()-> out.findById(PERIOD_ID));

        verify(trialPeriodRepository, times(1)).findById(PERIOD_ID);
    }

    @Test
    @DisplayName("Получение исключения, когда результат поиска - пустой лист (findAll)")
    void shouldThrowNotFoundExceptionWhenListOfAllIsEmpty(){
        when(trialPeriodRepository.findAll())
                .thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, ()-> out.findAll());

        verify(trialPeriodRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Получение исключения, когда результат поиска - пустой лист (findAllByOwnerId)")
    void shouldThrowNotFoundExceptionWhenListAnimalByOwnerIdIsEmpty(){
        when(trialPeriodRepository.findAllByOwnerId(OWNER_ID))
                .thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, ()-> out.findAllByOwnerId(OWNER_ID));

        verify(trialPeriodRepository, times(1)).findAllByOwnerId(OWNER_ID);
    }

    @Test
    @DisplayName("Обновление испытательного срока")
    void shouldReturnCorrectTrialPeriodWhenUpdate() {
        when(trialPeriodRepository.save(TRIAL_PERIOD))
                .thenReturn(TRIAL_PERIOD);

        assertEquals(TRIAL_PERIOD, out.update(TRIAL_PERIOD));

        verify(trialPeriodRepository, times(1)).save(TRIAL_PERIOD);
    }

    @Test
    @DisplayName("Удаление испытательного срока")
    void shouldReturnCorrectMessageWhenDelete(){
        when(trialPeriodRepository.findById(PERIOD_ID))
                .thenReturn(Optional.of(TRIAL_PERIOD));

        doNothing().when(trialPeriodRepository).delete(TRIAL_PERIOD);

        assertEquals("Испытательный срок удалён!", out.deleteById(PERIOD_ID));

        verify(trialPeriodRepository, times(1)).delete(TRIAL_PERIOD);
    }
}