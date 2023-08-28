package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.Report;
import com.teamwork.telegrambotanimalshelter.service.impl.ReportServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ReportController.class)
@ExtendWith(MockitoExtension.class)
class ReportControllerTest {
    String photoId = "123";
    String foodRation = "Test food";
    String stateOfHealth = "Good";
    String behaviorChange = "No changes";
    Long trialPeriodId = 1L;
    Long anyId = 1L;
    LocalDate localDate = LocalDate.now();
    Report createdReport = new Report(photoId, foodRation, stateOfHealth, behaviorChange, localDate, trialPeriodId);
    @MockBean
    ReportServiceImpl reportService;

    @Autowired
    MockMvc mockMvc;
    @Test
    @DisplayName("Создание нового отчёта")
    void create() throws Exception {
        Mockito.when(reportService.create(Mockito.any(Report.class))).thenReturn(createdReport);
        mockMvc.perform(post("/reports")
                        .param("photoId", "123")
                        .param("foodRation", "Test food")
                        .param("stateOfHealth", "Good")
                        .param("behaviorChange", "No changes")
                        .param("trialPeriodId", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.photoId", Matchers.is(photoId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.foodRation", Matchers.is(foodRation)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stateOfHealth", Matchers.is(stateOfHealth)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.behaviorChange", Matchers.is(behaviorChange)));
        Mockito.verify(reportService, times(1)).create(any(Report.class));

    }


    @Test
    @DisplayName("Получение всех отчётов")
    void getAll() throws Exception {
        when(reportService.getAll()).thenReturn(List.of(createdReport));
        mockMvc.perform(get("/reports"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].photoId", Matchers.is(photoId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].foodRation", Matchers.is(foodRation)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stateOfHealth", Matchers.is(stateOfHealth)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].behaviorChange", Matchers.is(behaviorChange)));
        verify(reportService, times(1)).getAll();
    }

    @Test
    @DisplayName("Найти отчет по его ID")
    void findByReportId() throws Exception {
        when(reportService.getById(any(Long.class))).thenReturn(createdReport);
        mockMvc.perform(get("/reports/report_id?id=" + anyId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.photoId", Matchers.is(photoId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.foodRation", Matchers.is(foodRation)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stateOfHealth", Matchers.is(stateOfHealth)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.behaviorChange", Matchers.is(behaviorChange)));
        verify(reportService, times(1)).getById(anyId);

    }
    @Test
    @DisplayName("Найти отчет по ID испытательного срока")
    void findByOwnerId() throws Exception {
        when(reportService.findAllByTrialPeriodId(any(Long.class))).thenReturn(List.of(createdReport));
        mockMvc.perform(get("/reports/owner_id?id=" + anyId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*.photoId").value("123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*.foodRation").value(foodRation))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*.behaviorChange").value(behaviorChange));
        verify(reportService, times(1)).findAllByTrialPeriodId(anyId);
    }
    @Test
    @DisplayName("Выкидываение ошибки если отчет не найден")
    void throwExceptionWhenReportIsNull() throws Exception {
        when(reportService.getById(any(Long.class))).thenReturn(null);
        mockMvc.perform(get("/reports/report_id?id=" + anyId))
                .andExpect(status().isNotFound());
        verify(reportService, times(1)).getById(anyId);
    }

    @Test
    @DisplayName("Обновление информации по отчёту")
    void update() throws Exception {
        when(reportService.getById(any(Long.class)))
                .thenReturn(createdReport);
        mockMvc.perform(put("/reports")
                .param("id", "123")
                .param("photoId", "123")
                .param("foodRation", "Test food")
                .param("stateOfHealth", "Good")
                .param("behaviorChange", "No changes")
                .param("trialPeriodId", "1"))
                .andExpect(status().isOk());
        verify(reportService, times(1)).update(any(Report.class));
    }


    @Test
    @DisplayName("Удаление отчёта из БД")
    void deleteTest() throws Exception{
        mockMvc.perform(delete("/reports?id=1"))
                .andExpect(status().isOk());
        verify(reportService, times(1)).deleteById(anyId);
    }
}