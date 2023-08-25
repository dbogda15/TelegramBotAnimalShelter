package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.exceptions.IncorrectArgumentException;
import com.teamwork.telegrambotanimalshelter.exceptions.ObjectAlreadyExistsException;
import com.teamwork.telegrambotanimalshelter.model.Report;
import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.enums.TrialPeriodType;
import com.teamwork.telegrambotanimalshelter.repository.ReportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.teamwork.telegrambotanimalshelter.model.enums.TrialPeriodType.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    ReportRepository reportRepository;
    @Mock
    TrialPeriodServiceImpl trialPeriodService;

    @InjectMocks
    ReportServiceImpl out;
    private static final String PHOTO_ID = "123";
    private static final String FOOD_RATION = "Test food";
    private static final String STATE_OF_HEALTH = "Good";
    private static final String BEHAVIOR_CHANGE = "No changes";
    private static final Long TRIAL_PERIOD_ID = 1L;
    private static final LocalDate LOCAL_DATE = LocalDate.now();
    private static final Long CORRECT_ID = 1L;

    private static final Report CREATED_REPORT = new Report(PHOTO_ID, FOOD_RATION, STATE_OF_HEALTH, BEHAVIOR_CHANGE,
            LOCAL_DATE, TRIAL_PERIOD_ID);


    private static final LocalDate DATE_OD_THE_START = LocalDate.now();
    private static final LocalDate DATE_OF_THE_END = LocalDate.now();
    private static final LocalDate LAST_DATE_OF_PERIOD = LocalDate.now();
    private static final Long OWNER_ID = 1L;
    private static final Long ANIMAL_ID = 1L;
    private static final AnimalType ANIMAL_TYPE = AnimalType.CAT;
    private static final List<Report> REPORTS = new ArrayList<>();
    private static final TrialPeriodType PERIOD_TYPE = IN_PROGRESS;
    private static final TrialPeriod TRIAL_PERIOD = new TrialPeriod(DATE_OD_THE_START, DATE_OF_THE_END,
            LAST_DATE_OF_PERIOD, OWNER_ID, ANIMAL_ID, ANIMAL_TYPE, REPORTS, PERIOD_TYPE);


    @Test
    @DisplayName("Создание отчета")
    void shouldReturnCorrectReportWhenCreate() {
        TRIAL_PERIOD.setLastDateOfReport(LocalDate.now().minusDays(1));
        when(trialPeriodService.findById(1L)).thenReturn(TRIAL_PERIOD);
        when(trialPeriodService.findAllByOwnerId(1L)).thenReturn(Collections.singletonList(TRIAL_PERIOD));
        when(reportRepository.save(CREATED_REPORT)).thenReturn(CREATED_REPORT);

        Report result = out.create(CREATED_REPORT);

        assertEquals(CREATED_REPORT, result);
        verify(trialPeriodService).findAllByOwnerId(1L);
        verify(reportRepository).save(CREATED_REPORT);
    }

    @Test
    @DisplayName("Выброс исключения, если прислали отчет уже")
    void shouldThrowNotFoundExceptionWhenReportShould() {
        when(trialPeriodService.findById(1L)).thenReturn(TRIAL_PERIOD);
        when(trialPeriodService.findAllByOwnerId(1L)).thenReturn(Collections.singletonList(TRIAL_PERIOD));
        assertThrows(ObjectAlreadyExistsException.class, () -> out.create(CREATED_REPORT));

    }

    @Test
    @DisplayName("Получение отчёта по его ID")
    void whenReturnValidReportById_thenReturn200() {
        when(reportRepository.findById(CORRECT_ID))
                .thenReturn(Optional.of(CREATED_REPORT));

        assertEquals(CREATED_REPORT, out.getById(CORRECT_ID));

        verify(reportRepository, times(1)).findById(CORRECT_ID);
    }

    @Test
    @DisplayName("Выброс исключения, если искомого отчёта нет")
    void shouldThrowNotFoundExceptionGetById() {
        when(reportRepository.findById(CORRECT_ID)).
                thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> out.getById(CORRECT_ID));
        verify(reportRepository, times(1)).findById(CORRECT_ID);
    }

    @Test
    @DisplayName("Получение всех отчетов")
    void getAll() {
        when(reportRepository.findAll())
                .thenReturn(List.of(CREATED_REPORT));

        assertEquals(List.of(CREATED_REPORT), out.getAll());

        verify(reportRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Выброс исключения, если искомого отчёта нет")
    void shouldThrowNotFoundExceptionWhenFindAll() {
        when(reportRepository.findAll()).
                thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class, () -> out.getAll());
        verify(reportRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Выдача отчета по пробному периоду")
    void shouldReturnCorrectReportByTrialPeriod() {
        when(reportRepository.findAllByTrialPeriodId(CORRECT_ID))
                .thenReturn(List.of(CREATED_REPORT));

        assertEquals(List.of(CREATED_REPORT), out.findAllByTrialPeriodId(CORRECT_ID));

        verify(reportRepository, times(1)).findAllByTrialPeriodId(CORRECT_ID);
    }

    @Test
    @DisplayName("Получение исключения, когда результат поиска - пустой лист (findAllByTrialPeriodId)")
    void shouldThrowNotFoundExceptionWhenFindAllByTrialPeriodId() {
        when(reportRepository.findAllByTrialPeriodId(CORRECT_ID))
                .thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> out.findAllByTrialPeriodId(CORRECT_ID));

        verify(reportRepository, times(1)).findAllByTrialPeriodId(CORRECT_ID);
    }

    @Test
    @DisplayName("Получение отчета попробному периоду и дате отчета")
    void shouldReturnCorrectDateOfReportAndTrialPeriodId() {
        when(reportRepository.findAllByDateOfReportAndTrialPeriodId(LOCAL_DATE, CORRECT_ID))
                .thenReturn(List.of(CREATED_REPORT));

        assertEquals(List.of(CREATED_REPORT), out.findAllByDateOfReportAndTrialPeriodId(LOCAL_DATE, CORRECT_ID));

        verify(reportRepository, times(1)).findAllByDateOfReportAndTrialPeriodId(LOCAL_DATE, CORRECT_ID);
    }

    @Test
    @DisplayName("Получение исключения, когда результат поиска - пустой лист (findAllByDateOfReportAndTrialPeriodId)")
    void shouldThrowNotFoundExceptionWhenFindAllByDateOfReportAndTrialPeriodId() {
        when(reportRepository.findAllByDateOfReportAndTrialPeriodId(LOCAL_DATE, CORRECT_ID))
                .thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> out.findAllByDateOfReportAndTrialPeriodId(LOCAL_DATE, CORRECT_ID));

        verify(reportRepository, times(1)).findAllByDateOfReportAndTrialPeriodId(LOCAL_DATE, CORRECT_ID);
    }

    @Test
    @DisplayName("Удаление отчета по ID")
    void shouldDeleteReportById() {
        when(reportRepository.findById(CORRECT_ID))
                .thenReturn(Optional.of(CREATED_REPORT));

        doNothing().when(reportRepository).delete(CREATED_REPORT);

        assertEquals("отчет удалён!", out.deleteById(CORRECT_ID));

        verify(reportRepository, times(1)).delete(CREATED_REPORT);
    }

    @Test
    @DisplayName("Получение исключения, когда результат поиска - пустой лист (findById)")
    void shouldThrowNotFoundExceptionWhenFindById() {
        when(reportRepository.findById(CORRECT_ID))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> out.deleteById(CORRECT_ID));

        verify(reportRepository, times(1)).findById(CORRECT_ID);
    }

    @Test
    @DisplayName("Обновление отчета")
    void shouldReturnCorrectReportAfterUpdate() {
        when(reportRepository.save(CREATED_REPORT)).thenReturn(CREATED_REPORT);
        assertEquals(CREATED_REPORT, out.update(CREATED_REPORT));
        verify(reportRepository, times(1)).save(CREATED_REPORT);
    }

    @Test
    public void testSplit_validMessage_returnsList() {
        // Arrange
        String message = "Рацион питания: Хороший;\nОбщее самочувствие: Отличное;\nИзменение поведения: Нет;";

        // Act
        List<String> result = out.split(message);

        // Assert
        assertEquals(List.of("Хороший", "Отличное", "Нет"), result);
    }

    @Test
    public void testSplit_invalidMessage_throwsException() {
        // Arrange
        String message = "Invalid message";

        // Act & Assert
        assertThrows(IncorrectArgumentException.class, () -> out.split(message));
    }

    @Test
    public void testSplit_missingKeywords_throwsException() {
        // Arrange
        String message = "Some message";

        // Act & Assert
        assertThrows(IncorrectArgumentException.class, () -> out.split(message));
    }
}