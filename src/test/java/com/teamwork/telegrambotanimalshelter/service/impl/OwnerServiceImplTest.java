package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.exceptions.IncorrectArgumentException;
import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.enums.TrialPeriodType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.repository.OwnerRepository;
import com.teamwork.telegrambotanimalshelter.service.AnimalService;
import com.teamwork.telegrambotanimalshelter.service.TrialPeriodService;
import org.checkerframework.checker.units.qual.C;
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
class OwnerServiceImplTest {
    @Mock
    OwnerRepository ownerRepository;

    @Mock
    AnimalService animalService;

    @Mock
    TrialPeriodService trialPeriodService;

    @InjectMocks
    OwnerServiceImpl out;

    static final Long ANIMAL_ID = 1L;
    static final Long OWNER_ID = 1L;
    static final Long CAT_OWNER_ID = 2L;
    static final Long CHAT_ID = 2323456L;
    static final Long CAT_OWNER_CHAT_ID = 23234343L;
    static final Owner CORRECT_OWNER = new Owner(OWNER_ID, CHAT_ID, "Name", "89177534345", AnimalType.CAT, new ArrayList<>(), new ArrayList<>());
    static final Owner NEW_OWNER = new Owner("Name", "89177534345");
    static final Animal CAT =  new Animal(ANIMAL_ID, AnimalType.CAT, "Cat", 5);
    static final Owner OWNER_WITHOUT_CAT = new Owner(CAT_OWNER_ID, CAT_OWNER_CHAT_ID, "Name", "89177534345");
    static final Owner CAT_OWNER = new Owner(CAT_OWNER_ID, CAT_OWNER_CHAT_ID, "Name", "89177534345", AnimalType.CAT, List.of(CAT), new ArrayList<>());
    static final Animal BUSY_CAT = new Animal(3L, AnimalType.CAT,"Barsik", 5, CORRECT_OWNER);
    static final TrialPeriod TRIAL_PERIOD = new TrialPeriod(LocalDate.now(), LocalDate.now().plusDays(30), LocalDate.now().minusDays(1),
            CAT_OWNER_ID, ANIMAL_ID, AnimalType.CAT, new ArrayList<>(), TrialPeriodType.IN_PROGRESS);

    @Test
    @DisplayName("Создание нового владельца")
    void shouldReturnCorrectOwnerWithoutANimalWhenCreate(){
        when(ownerRepository.save(NEW_OWNER))
                .thenReturn(NEW_OWNER);

        assertEquals(NEW_OWNER, out.create(NEW_OWNER));

        verify(ownerRepository, times(1)).save(NEW_OWNER);
    }

    @Test
    @DisplayName("Создание владельца с животным")
    void shouldReturnCorrectOwnerWithAnimalWhenCreate(){
        when(animalService.getById(ANIMAL_ID))
                .thenReturn(CAT);
        when(ownerRepository.findById(CAT_OWNER_ID))
                .thenReturn(Optional.of(OWNER_WITHOUT_CAT));
        when(trialPeriodService.create(TRIAL_PERIOD))
                .thenReturn(TRIAL_PERIOD);
        when(ownerRepository.save(OWNER_WITHOUT_CAT))
                .thenReturn(CAT_OWNER);

        assertEquals(CAT_OWNER, out.create(CAT_OWNER_ID, ANIMAL_ID));
    }


    @Test
    @DisplayName("Поиск владельца по ID")
    void shouldReturnCorrectOwnerById(){
        when(ownerRepository.findById(OWNER_ID))
                .thenReturn(Optional.of(CORRECT_OWNER));

        assertEquals(CORRECT_OWNER, out.getById(OWNER_ID));

        verify(ownerRepository, times(1)).findById(OWNER_ID);
    }

    @Test
    @DisplayName("Проверка исключения когда владельца не существует")
    void shouldThrowNotFoundExceptionWhenOwnerNotExists(){
        when(ownerRepository.findById(OWNER_ID))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> out.getById(OWNER_ID));

        verify(ownerRepository, times(1)).findById(OWNER_ID);
    }

    @Test
    @DisplayName("Удаление животного по ID")
    void shouldDeleteOwnerById() {
        when(ownerRepository.findById(OWNER_ID))
                .thenReturn(Optional.of(CORRECT_OWNER));

        doNothing().when(ownerRepository).delete(CORRECT_OWNER);

        assertEquals("Владелец удален", out.delete(OWNER_ID));

        verify(ownerRepository, times(1)).findById(OWNER_ID);
    }

    @Test
    @DisplayName("Получение списка всех владельцев")
    void shouldReturnCorrectListOfAllOwners() {
        when(ownerRepository.findAll())
                .thenReturn(List.of(CORRECT_OWNER));

        assertEquals(List.of(CORRECT_OWNER), out.getAll());

        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Поиск владельца по chat ID")
    void shouldReturnCorrectOwnerByChatId(){
        when(ownerRepository.getOwnerByChatId(CHAT_ID))
                .thenReturn(CORRECT_OWNER);

        assertEquals(CORRECT_OWNER, out.getByChatId(CHAT_ID));

        verify(ownerRepository, times(1)).getOwnerByChatId(CHAT_ID);
    }

    @Test
    @DisplayName("Обновление информации о владельце")
    void shouldReturnCorrectOwnerAfterUpdate(){
        when(ownerRepository.save(CORRECT_OWNER))
                .thenReturn(CORRECT_OWNER);

        assertEquals(CORRECT_OWNER, out.update(CORRECT_OWNER));

        verify(ownerRepository, times(1)).save(CORRECT_OWNER);
    }

    @Test
    @DisplayName("Проверка исключения когда не найден владелец при удалении")
    void shouldThrowNotFoundExceptionWhenOwnerIsEmpty(){
        when(ownerRepository.findById(OWNER_ID))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> out.delete(OWNER_ID));

        verify(ownerRepository, times(1)).findById(OWNER_ID);
    }

    @Test
    @DisplayName("Список животных владельца по его ID")
    void shouldReturnListOfAnimalByOwnerId(){
        when(ownerRepository.findById(CAT_OWNER.getId()))
                .thenReturn(Optional.of(CAT_OWNER));

        List<Animal> result = out.getAnimalByOwnerId(CAT_OWNER.getId());
        assertEquals(List.of(CAT), result);

        verify(ownerRepository, times(1)).findById(CAT_OWNER.getId());
    }

    @Test
    @DisplayName("Проверка исключения когда владельца не существует")
    void shouldThrowNotFoundExceptionWhenOwnerDoesntExist(){
        when(ownerRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()-> out.getById(100L));
    }
}