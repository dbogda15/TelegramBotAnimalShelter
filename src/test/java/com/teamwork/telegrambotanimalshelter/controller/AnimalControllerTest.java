package com.teamwork.telegrambotanimalshelter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.service.OwnerService;
import com.teamwork.telegrambotanimalshelter.service.impl.AnimalServiceImpl;
import com.teamwork.telegrambotanimalshelter.service.impl.OwnerServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnimalController.class)
@ExtendWith(MockitoExtension.class)
class AnimalControllerTest {

    @MockBean
    AnimalServiceImpl animalService;

    @MockBean
    OwnerServiceImpl ownerService;
    @Autowired
    MockMvc mockMvc;

    static final Owner OWNER = new Owner(123L, "Name", "89171231213", new ArrayList<>());
    static final Animal ANIMAL = new Animal(1L, AnimalType.CAT, "Name", 5, OWNER, 1L);
    static final Animal NEW_ANIMAL = new Animal(AnimalType.CAT, "Name", 5);
    static final Animal UPDATED_ANIMAL = new Animal(1L, AnimalType.CAT, "New name", 10, OWNER, 1L);

    @Test
    @DisplayName("Создание новой собаки")
    void whenValidInput_thenReturn200() throws Exception {
        when(animalService.create(any(Animal.class))).thenReturn(NEW_ANIMAL);
        mockMvc.perform(post("/animals")
                        .param("animalType", "CAT")
                        .param("name", "Name")
                        .param("age", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("animalType").value("CAT"))
                .andExpect(jsonPath("name").value("Name"))
                .andExpect(jsonPath("age").value(5));
        verify(animalService, times(1)).create(any(Animal.class));
    }

    @Test
    @DisplayName("Удаление животного")
    void whenValidDelete_thenReturn200() throws Exception {
        mockMvc.perform(delete("/animals"))
                .andExpect(status().isOk());
        verify(animalService, times(1)).delete(ANIMAL.getId());
    }

    @Test
    @DisplayName("Обновление животного")
    void whenValidUpdate_thenReturn200() throws Exception {
        when(animalService.update(any(Animal.class)))
                .thenReturn(UPDATED_ANIMAL);
        mockMvc.perform(put("/animals")
                        .param("id", "1L")
                        .param("name", "New name")
                        .param("age", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("animalType").value("CAT"))
                .andExpect(jsonPath("name").value("New name"))
                .andExpect(jsonPath("age").value(10))
                .andExpect(jsonPath("owner").value(OWNER))
                .andExpect(jsonPath("shelterId").value(2L));

        verify(animalService, times(1)).update(any(Animal.class));
    }
}