package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.shelters.Shelter;
import com.teamwork.telegrambotanimalshelter.service.impl.ShelterServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShelterController.class)
@ExtendWith(MockitoExtension.class)
class ShelterControllerTest {

    @MockBean
    ShelterServiceImpl shelterService;
    @Autowired
    MockMvc mockMvc;

    static final Long SHELTER_ID = 1L;
    static final Shelter CORRECT_SHELTER = new Shelter(SHELTER_ID, "name", "location", "timetable", "aboutMe",
            "security", "safetyAdvice");
    static final Animal REX = new Animal(3L, AnimalType.DOG, "Rex", 5, 2L);


    @Test
    @DisplayName("Создание приюта")
    void whenValidInput_thenReturn200() throws Exception {
        when(shelterService.create(any(Shelter.class))).thenReturn(CORRECT_SHELTER);
        mockMvc.perform(post("/shelter")
                        .param("name", "name")
                        .param("location", "location")
                        .param("timetable", "timetable")
                        .param("aboutMe", "aboutMe")
                        .param("security", "security")
                        .param("safetyAdvice", "safetyAdvice")
                        .param("shelterType", "CAT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("name"))
                .andExpect(jsonPath("location").value("location"))
                .andExpect(jsonPath("timetable").value("timetable"))
                .andExpect(jsonPath("aboutMe").value("aboutMe"))
                .andExpect(jsonPath("security").value("security"))
                .andExpect(jsonPath("safetyAdvice").value("safetyAdvice"))
                .andExpect(jsonPath("shelterType").value("CAT"));
        verify(shelterService, times(1)).create(any(Shelter.class));
    }

    @Test
    @DisplayName("Удаление приюта")
    void whenValidDelete_thenReturn200() throws Exception {
        mockMvc.perform(delete("/shelter?id=1"))
                .andExpect(status().isOk());
        verify(shelterService, times(1)).delete(SHELTER_ID);
    }

    @Test
    @DisplayName("Получение приюта по его ID")
    void whenReturnValidShelterById_thenReturn200() throws Exception {
        when(shelterService.getById(SHELTER_ID))
                .thenReturn(CORRECT_SHELTER);
        mockMvc.perform(get("/shelter/id?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value("name"))
                .andExpect(jsonPath("location").value("location"))
                .andExpect(jsonPath("timetable").value("timetable"))
                .andExpect(jsonPath("aboutMe").value("aboutMe"))
                .andExpect(jsonPath("security").value("security"))
                .andExpect(jsonPath("safetyAdvice").value("safetyAdvice"));

        verify(shelterService, times(1)).getById(SHELTER_ID);
    }

    @Test
    @DisplayName("Получение списка животных по ID приюта")
    void whenReturnValidListOfAllDog_thenReturn200() throws Exception {
        when(shelterService.getAnimals(SHELTER_ID))
                .thenReturn((List.of(REX)));
        mockMvc.perform(get("/shelter/animal_list?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].animalType").value("DOG"))
                .andExpect(jsonPath("$[0].name").value("Rex"))
                .andExpect(jsonPath("$[0].age").value(5));
        verify(shelterService, times(1)).getAnimals(SHELTER_ID);
    }


    @Test
    @DisplayName("Обновление данных по приюту")
    void whenValidUpdate_thenReturn200() throws Exception {
        when(shelterService.getById(SHELTER_ID))
                .thenReturn(CORRECT_SHELTER);
        when(shelterService.update(any(Shelter.class)))
                .thenReturn(CORRECT_SHELTER);
        mockMvc.perform(put("/shelter")
                        .param("id", "1")
                        .param("name", "name")
                        .param("location", "location")
                        .param("timetable", "timetable")
                        .param("aboutMe", "aboutMe")
                        .param("security", "security")
                        .param("safetyAdvice", "safetyAdvice")
                        .param("shelterType", "CAT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("name"))
                .andExpect(jsonPath("location").value("location"))
                .andExpect(jsonPath("timetable").value("timetable"))
                .andExpect(jsonPath("aboutMe").value("aboutMe"))
                .andExpect(jsonPath("security").value("security"))
                .andExpect(jsonPath("safetyAdvice").value("safetyAdvice"))
                .andExpect(jsonPath("shelterType").value("CAT"));

        verify(shelterService, times(1)).update(any(Shelter.class));
    }
}