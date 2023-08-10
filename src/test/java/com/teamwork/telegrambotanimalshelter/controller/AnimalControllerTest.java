package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.service.impl.AnimalServiceImpl;
import com.teamwork.telegrambotanimalshelter.service.impl.OwnerServiceImpl;
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

@WebMvcTest(AnimalController.class)
@ExtendWith(MockitoExtension.class)
class AnimalControllerTest {

    @MockBean
    AnimalServiceImpl animalService;

    @MockBean
    OwnerServiceImpl ownerService;
    @Autowired
    MockMvc mockMvc;

    static final Long ID_1 = 1L;
    static final Owner OWNER = new Owner(1L, "Diana", "89171231213");
    static final Animal ANIMAL = new Animal(ID_1, AnimalType.CAT, "Name", 5, OWNER, 1L);
    static final Animal NEW_ANIMAL = new Animal(AnimalType.CAT, "Name", 5);
    static final Animal UPDATED_ANIMAL = new Animal(ID_1, AnimalType.CAT, "New name", 10, 1L);
    static final Animal DONUT = new Animal(2L, AnimalType.CAT, "Donut", 4, 1L);
    static final Animal DONUT_WITH_OWNER = new Animal(2L, AnimalType.CAT, "Donut", 4, OWNER, 1L);
    static final Animal REX = new Animal(3L, AnimalType.DOG, "Rex", 5, 2L);
    static final Animal BOB = new Animal(4L, AnimalType.DOG, "Bob", 7, 2L);


    @Test
    @DisplayName("Создание нового животного")
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
        mockMvc.perform(delete("/animals/id?id=1"))
                .andExpect(status().isOk());
        verify(animalService, times(1)).delete(ANIMAL.getId());
    }

    @Test
    @DisplayName("Обновление животного")
    void whenValidUpdate_thenReturn200() throws Exception {
        when(animalService.update(any(Animal.class)))
                .thenReturn(UPDATED_ANIMAL);
        mockMvc.perform(put("/animals")
                        .param("id", "1")
                        .param("animalType", "CAT")
                        .param("name", "New name")
                        .param("age", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("animalType").value("CAT"))
                .andExpect(jsonPath("name").value("New name"))
                .andExpect(jsonPath("age").value(10));

        verify(animalService, times(1)).update(any(Animal.class));
    }

    @Test
    @DisplayName("Получение списка всех животных")
    void whenReturnValidListOfAllAnimal_thenReturn200() throws Exception {
        when(animalService.getAll())
                .thenReturn(List.of(UPDATED_ANIMAL, DONUT));
        mockMvc.perform(get("/animals/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].animalType").value("CAT"))
                .andExpect(jsonPath("$[0].name").value("New name"))
                .andExpect(jsonPath("$[0].age").value(10))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].animalType").value("CAT"))
                .andExpect(jsonPath("$[1].name").value("Donut"))
                .andExpect(jsonPath("$[1].age").value(4));
        verify(animalService, times(1)).getAll();
    }

    @Test
    @DisplayName("Получение списка всех свободных животных")
    void whenReturnValidListOfFreeAnimal_thenReturn200() throws Exception {
        when(animalService.freeAnimalList())
                .thenReturn(List.of(UPDATED_ANIMAL, DONUT));
        mockMvc.perform(get("/animals/free_animal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].animalType").value("CAT"))
                .andExpect(jsonPath("$[0].name").value("New name"))
                .andExpect(jsonPath("$[0].age").value(10))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].animalType").value("CAT"))
                .andExpect(jsonPath("$[1].name").value("Donut"))
                .andExpect(jsonPath("$[1].age").value(4));
        verify(animalService, times(1)).freeAnimalList();
    }

    @Test
    @DisplayName("Получение животного по его ID")
    void whenReturnValidAnimalById_thenReturn200() throws Exception {
        when(animalService.getById(ID_1))
                .thenReturn(UPDATED_ANIMAL);
        mockMvc.perform(get("/animals/animalId?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("animalType").value("CAT"))
                .andExpect(jsonPath("name").value("New name"))
                .andExpect(jsonPath("age").value(10));

        verify(animalService, times(1)).getById(ID_1);
    }

    @Test
    @DisplayName("Получение списка всех котов")
    void whenReturnValidListOfAllCat_thenReturn200() throws Exception {
        when(animalService.getAnimalsByType(AnimalType.CAT))
                .thenReturn(List.of(UPDATED_ANIMAL, DONUT));
        mockMvc.perform(get("/animals/cat_list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].animalType").value("CAT"))
                .andExpect(jsonPath("$[0].name").value("New name"))
                .andExpect(jsonPath("$[0].age").value(10))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].animalType").value("CAT"))
                .andExpect(jsonPath("$[1].name").value("Donut"))
                .andExpect(jsonPath("$[1].age").value(4));
        verify(animalService, times(1)).getAnimalsByType(AnimalType.CAT);
    }

    @Test
    @DisplayName("Получение списка всех собак")
    void whenReturnValidListOfAllDog_thenReturn200() throws Exception {
        when(animalService.getAnimalsByType(AnimalType.DOG))
                .thenReturn(List.of(REX, BOB));
        mockMvc.perform(get("/animals/dog_list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].animalType").value("DOG"))
                .andExpect(jsonPath("$[0].name").value("Rex"))
                .andExpect(jsonPath("$[0].age").value(5))

                .andExpect(jsonPath("$[1].id").value(4))
                .andExpect(jsonPath("$[1].animalType").value("DOG"))
                .andExpect(jsonPath("$[1].name").value("Bob"))
                .andExpect(jsonPath("$[1].age").value(7));
        verify(animalService, times(1)).getAnimalsByType(AnimalType.DOG);
    }

    @Test
    @DisplayName("Назначить животному хозяина")
    void whenSetOwnerToAnimal_thenReturn200 () throws Exception {
        when(animalService.setOwner(2L, 1L))
                .thenReturn(DONUT_WITH_OWNER);
        mockMvc.perform(put("/animals/owner_id?id=2&owner_id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(2))
                .andExpect(jsonPath("animalType").value("CAT"))
                .andExpect(jsonPath("name").value("Donut"))
                .andExpect(jsonPath("age").value(4));

        verify(animalService, times(1)).setOwner(2L, 1L);

    }

    @Test
    @DisplayName("Назначить животному хозяина")
    void whenSetShelterToAnimal_thenReturn200 () throws Exception {
        when(animalService.setShelterId(2L, 1L))
                .thenReturn(DONUT);
        mockMvc.perform(put("/animals/shelter_id")
                        .param("animalId","2")
                        .param("shelterId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(2))
                .andExpect(jsonPath("animalType").value("CAT"))
                .andExpect(jsonPath("name").value("Donut"))
                .andExpect(jsonPath("age").value(4));

        verify(animalService, times(1)).setShelterId(2L, 1L);

    }
}