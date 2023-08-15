package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.enums.TrialPeriodType;

import com.teamwork.telegrambotanimalshelter.service.TrialPeriodService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrialPeriodController.class)
@ExtendWith(MockitoExtension.class)
class TrialPeriodControllerTest {

    @MockBean
    TrialPeriodService trialPeriodService;

    @Autowired
    MockMvc mockMvc;

    final static Long OWNER_ID = 1L;
    final static Long ANIMAL_ID = 1L;
    final static Long PERIOD_ID = 1L;
    final static TrialPeriod TRIAL_PERIOD = new TrialPeriod(PERIOD_ID, LocalDate.now(), LocalDate.now().plusDays(30), LocalDate.now().minusDays(1),
            OWNER_ID, ANIMAL_ID, AnimalType.CAT, new ArrayList<>(), TrialPeriodType.IN_PROGRESS);

    @Test
    @DisplayName("Создание испытательного срока")
    void whenValidCreate_thenReturn200() throws Exception {
        when(trialPeriodService.create(any(TrialPeriod.class)))
                .thenReturn(TRIAL_PERIOD);

        mockMvc.perform(post("/trial_period")
                        .param("dateOfTheStart", LocalDate.now().toString())
                        .param("ownerId", OWNER_ID.toString())
                        .param("animalId", ANIMAL_ID.toString())
                        .param("animalType", "CAT")
                        .param("type", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(PERIOD_ID))
                .andExpect(jsonPath("dateOfTheStart").value(LocalDate.now().toString()))
                .andExpect(jsonPath("dateOfTheEnd").value(LocalDate.now().plusDays(30).toString()))
                .andExpect(jsonPath("lastDateOfReport").value(LocalDate.now().minusDays(1).toString()))
                .andExpect(jsonPath("ownerId").value(OWNER_ID))
                .andExpect(jsonPath("animalId").value(ANIMAL_ID))
                .andExpect(jsonPath("animalType").value("CAT"))
                .andExpect(jsonPath("reports").value(new ArrayList<>()))
                .andExpect(jsonPath("periodType").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("Удаление испытательного срока")
    void shouldReturnCorrectMessageWhenDelete_thenReturn200() throws Exception {
        mockMvc.perform(delete("/trial_period")
                        .param("id", String.valueOf(PERIOD_ID)))
                .andExpect(status().isOk());

        verify(trialPeriodService, times(1)).deleteById(PERIOD_ID);
    }

    @Test
    @DisplayName("Получение испытательного срока по его ID")
    void whenGetById_thenReturn200() throws Exception {
        when(trialPeriodService.findById(PERIOD_ID))
                .thenReturn(TRIAL_PERIOD);

        mockMvc.perform(get("/trial_period/id")
                        .param("id", String.valueOf(PERIOD_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(PERIOD_ID))
                .andExpect(jsonPath("dateOfTheStart").value(LocalDate.now().toString()))
                .andExpect(jsonPath("dateOfTheEnd").value(LocalDate.now().plusDays(30).toString()))
                .andExpect(jsonPath("lastDateOfReport").value(LocalDate.now().minusDays(1).toString()))
                .andExpect(jsonPath("ownerId").value(OWNER_ID))
                .andExpect(jsonPath("animalId").value(ANIMAL_ID))
                .andExpect(jsonPath("animalType").value("CAT"))
                .andExpect(jsonPath("reports").value(new ArrayList<>()))
                .andExpect(jsonPath("periodType").value("IN_PROGRESS"));

        verify(trialPeriodService, times(1)).findById(PERIOD_ID);
    }

    @Test
    @DisplayName("Получение всех испытательных сроков")
    void whenGetAll_thenReturn200() throws Exception {
        when(trialPeriodService.findAll())
                .thenReturn(List.of(TRIAL_PERIOD));

        mockMvc.perform(get("/trial_period/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(PERIOD_ID))
                .andExpect(jsonPath("$[0].dateOfTheStart").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$[0].dateOfTheEnd").value(LocalDate.now().plusDays(30).toString()))
                .andExpect(jsonPath("$[0].lastDateOfReport").value(LocalDate.now().minusDays(1).toString()))
                .andExpect(jsonPath("$[0].ownerId").value(OWNER_ID))
                .andExpect(jsonPath("$[0].animalId").value(ANIMAL_ID))
                .andExpect(jsonPath("$[0].animalType").value("CAT"))
                .andExpect(jsonPath("$[0].reports").value(new ArrayList<>()))
                .andExpect(jsonPath("$[0].periodType").value("IN_PROGRESS"));

        verify(trialPeriodService, times(1)).findAll();
    }

    @Test
    @DisplayName("Получение всех испытательных сроков по ID владельца")
    void whenGetAllByOwnerId_thenReturn200() throws Exception {
        when(trialPeriodService.findAllByOwnerId(OWNER_ID))
                .thenReturn(List.of(TRIAL_PERIOD));

        mockMvc.perform(get("/trial_period/owner_id")
                        .param("ownerId", OWNER_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(PERIOD_ID))
                .andExpect(jsonPath("$[0].dateOfTheStart").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$[0].dateOfTheEnd").value(LocalDate.now().plusDays(30).toString()))
                .andExpect(jsonPath("$[0].lastDateOfReport").value(LocalDate.now().minusDays(1).toString()))
                .andExpect(jsonPath("$[0].ownerId").value(OWNER_ID))
                .andExpect(jsonPath("$[0].animalId").value(ANIMAL_ID))
                .andExpect(jsonPath("$[0].animalType").value("CAT"))
                .andExpect(jsonPath("$[0].reports").value(new ArrayList<>()))
                .andExpect(jsonPath("$[0].periodType").value("IN_PROGRESS"));

        verify(trialPeriodService, times(1)).findAllByOwnerId(OWNER_ID);
    }

    @Test
    @DisplayName("Обновление испытательного срока")
    void whenUpdate_thenReturn200() throws Exception {
        when(trialPeriodService.findById(PERIOD_ID))
                .thenReturn(TRIAL_PERIOD);
        when(trialPeriodService.update(any(TrialPeriod.class)))
                .thenReturn(TRIAL_PERIOD);

        mockMvc.perform(put("/trial_period")
                        .param("id", "1")
                        .param("ownerId", "1")
                        .param("animalId", "1")
                        .param("animalType", "CAT")
                        .param("type", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(PERIOD_ID))
                .andExpect(jsonPath("dateOfTheStart").value(LocalDate.now().toString()))
                .andExpect(jsonPath("dateOfTheEnd").value(LocalDate.now().plusDays(30).toString()))
                .andExpect(jsonPath("lastDateOfReport").value(LocalDate.now().minusDays(1).toString()))
                .andExpect(jsonPath("ownerId").value(OWNER_ID))
                .andExpect(jsonPath("animalId").value(ANIMAL_ID))
                .andExpect(jsonPath("animalType").value("CAT"))
                .andExpect(jsonPath("reports").value(new ArrayList<>()))
                .andExpect(jsonPath("periodType").value("IN_PROGRESS"));

        verify(trialPeriodService, times(1)).update(any(TrialPeriod.class));
    }
}