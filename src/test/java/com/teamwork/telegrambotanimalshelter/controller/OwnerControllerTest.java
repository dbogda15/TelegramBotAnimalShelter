package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.service.AnimalService;
import com.teamwork.telegrambotanimalshelter.service.OwnerService;
import com.teamwork.telegrambotanimalshelter.service.ReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OwnerController.class)
@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @MockBean
    AnimalService animalService;

    @MockBean
    OwnerService ownerService;

    @MockBean
    ReportService reportService;

    @Autowired
    MockMvc mockMvc;
    final static Long DOG_ID = 1L;
    final static Long OWNER_ID = 1L;
    final static Animal NEW_DOG = new Animal(DOG_ID, AnimalType.DOG, "Dog", 5);
    final static Animal BOB = new Animal(2L, AnimalType.DOG, "Bob", 5);
    final static Animal REX = new Animal(3L, AnimalType.DOG, "Rex", 6);
    final static Owner NEW_OWNER = new Owner("Name", "89170099888");
    final static Owner OWNER = new Owner(OWNER_ID, 333L, "Name", "89170099888", AnimalType.DOG, new ArrayList<>(), new ArrayList<>());
    final static Owner DOG_OWNER = new Owner(OWNER_ID, 333L, "Name", "89170099888", AnimalType.DOG, List.of(NEW_DOG, BOB), new ArrayList<>());
    final static Owner REX_OWNER = new Owner(OWNER_ID, 333L, "Ivan", "89170099800", AnimalType.DOG, List.of(REX), new ArrayList<>());
    @Test
    @DisplayName("Создание нового владельца")
    void whenValidInput_thenReturn200() throws Exception {
        when(ownerService.create(any(Owner.class)))
                .thenReturn(NEW_OWNER);
        mockMvc.perform(post("/owners")
                        .param("name", "Name")
                        .param("phone", "89170099888"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Name"))
                .andExpect(jsonPath("phone").value("89170099888"));

        verify(ownerService, times(1)).create(NEW_OWNER);
    }

    @Test
    @DisplayName("Создание владельца с животным")
    void whenCreateOwnerWithAnimal_thenReturn200() throws Exception {
        when(ownerService.create(OWNER_ID, REX.getId()))
                .thenReturn(REX_OWNER);
        mockMvc.perform(post("/owners/animal_owner")
                .param("ownerId", OWNER_ID.toString())
                .param("animalId", REX.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(OWNER_ID))
                .andExpect(jsonPath("name").value("Ivan"))
                .andExpect(jsonPath("phone").value("89170099800"));

        verify(ownerService, times(1)).create(OWNER_ID, REX.getId());
    }


    @Test
    @DisplayName("Получение списка животных по ID владельца")
    void whenGetValidAnimalByOwnerId_thenReturn200() throws Exception {
        when(ownerService.getById(OWNER_ID))
                .thenReturn(DOG_OWNER);

        when(ownerService.getAnimalByOwnerId(DOG_OWNER.getId()))
                .thenReturn(List.of(NEW_DOG, BOB));

        mockMvc.perform(get("/owners/animal")
                        .param("owner_id", String.valueOf(OWNER_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(NEW_DOG.getId()))
                .andExpect(jsonPath("$[0].animalType").value("DOG"))
                .andExpect(jsonPath("$[0].name").value(NEW_DOG.getName()))
                .andExpect(jsonPath("$[0].age").value(NEW_DOG.getAge()))

                .andExpect(jsonPath("$[1].id").value(BOB.getId()))
                .andExpect(jsonPath("$[1].animalType").value("DOG"))
                .andExpect(jsonPath("$[1].name").value(BOB.getName()))
                .andExpect(jsonPath("$[1].age").value(BOB.getAge()));

        verify(ownerService, times(1)).getAnimalByOwnerId(OWNER_ID);
    }

    @Test
    @DisplayName("Получение владельца по ID")
    void whenGetValidOwnerById_thenReturn200() throws Exception {
        when(ownerService.getById(OWNER_ID))
                .thenReturn(OWNER);

        mockMvc.perform(get("/owners/owner_id")
                .param("id", String.valueOf(OWNER_ID))
                .param("name", "Name")
                .param("phone", "89170099888"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(OWNER_ID))
                .andExpect(jsonPath("name").value(OWNER.getName()))
                .andExpect(jsonPath("phone").value(OWNER.getPhone()));

        verify(ownerService, times(1)).getById(OWNER_ID);
    }

    @Test
    @DisplayName("Получение всех владельцев")
    void whenGetAll_thenReturn200() throws Exception {
        when(ownerService.getAll())
                .thenReturn(List.of(OWNER));

        mockMvc.perform(get("/owners/get_all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(OWNER.getId()))
                .andExpect(jsonPath("$[0].name").value(OWNER.getName()))
                .andExpect(jsonPath("$[0].phone").value(OWNER.getPhone()));

        verify(ownerService, times(1)).getAll();
    }

    @Test
    @DisplayName("Удаление владельца по ID")
    void whenDelete_thenReturn200() throws Exception {
        mockMvc.perform(delete("/owners")
                .param("id", OWNER_ID.toString()))
                .andExpect(status().isOk());

        verify(ownerService, times(1)).delete(OWNER_ID);
    }

    @Test
    @DisplayName("Обновление информации о владельце по его ID")
    void update() throws Exception {
        when(ownerService.getById(OWNER_ID))
                .thenReturn(OWNER);
        when(ownerService.update(any(Owner.class)))
                .thenReturn(OWNER);

        mockMvc.perform(put("/owners")
                        .param("id", OWNER_ID.toString())
                        .param("name", "Name")
                        .param("chatId", "333" )
                        .param("phone", "89170099888"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(OWNER_ID))
                .andExpect(jsonPath("name").value(OWNER.getName()))
                .andExpect(jsonPath("chatId").value(OWNER.getChatId()))
                .andExpect(jsonPath("phone").value(OWNER.getPhone()));
        verify(ownerService, times(1)).update(any(Owner.class));

    }
}