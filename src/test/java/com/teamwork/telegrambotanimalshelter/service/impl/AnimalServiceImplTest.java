package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.repository.AnimalRepository;
import com.teamwork.telegrambotanimalshelter.repository.ShelterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceImplTest {
    public static final Long CORRECT_ID = 1L;
    public static final Long INCORRECT_ID = -1L;
    public static final Long SHELTER_ID = 1L;
    public static final Long CHAT_ID = 123L;
    public static final String CORRECT_NAME = "Cat";
    public static final Integer CORRECT_AGE = 1;
    public static final AnimalType CORRECT_TYPE = AnimalType.CAT;
    public static final Long CORRECT_OWNER_ID = 2L;
    public static final String CORRECT_OWNER_PHONE = "89991234567";
    public static final String CORRECT_OWNER_NAME = "Name";
    public static final AnimalType OWNER_TYPE = CORRECT_TYPE;
    public static final Owner CORRECT_OWNER = new Owner(CORRECT_OWNER_ID, CHAT_ID, CORRECT_OWNER_NAME, CORRECT_OWNER_PHONE, OWNER_TYPE, new ArrayList<>(), new ArrayList<>());
    public static final Animal CORRECT_ANIMAL = new Animal(CORRECT_ID, CORRECT_TYPE, CORRECT_NAME, CORRECT_AGE, CORRECT_OWNER, SHELTER_ID);
    public static final Animal CORRECT_ANIMAL_WITHOUT_OWNER = new Animal(CORRECT_ID, CORRECT_TYPE, CORRECT_NAME, CORRECT_AGE);

    public static final Animal FREE_ANIMAL = new Animal(CORRECT_ID, CORRECT_TYPE, CORRECT_NAME, CORRECT_AGE, null, null);
    @Mock
    AnimalRepository animalRepository;

    @InjectMocks
    AnimalServiceImpl out;

    @Test
    @DisplayName("Получение животного по его ID")
    void shouldReturnCorrectAnimalIfIdIsCorrect() {
        when(animalRepository.findById(CORRECT_ID))
                .thenReturn(Optional.of(CORRECT_ANIMAL));

        assertEquals(CORRECT_ANIMAL, out.getById(CORRECT_ID));

        verify(animalRepository, times(1)).findById(CORRECT_ID);
    }

    @Test
    @DisplayName("Выброс нового исключения из репозитория")
    void shouldThrowNotFoundExceptionWhenAnimalNotFoundById() {
        when(animalRepository.findById(INCORRECT_ID))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> out.getById(INCORRECT_ID));
    }

    @Test
    @DisplayName("Проверка сохранения нового животного")
    void shouldReturnCorrectAnimalWhenCreate() {
        when(animalRepository.save(CORRECT_ANIMAL))
                .thenReturn(CORRECT_ANIMAL);

        assertEquals(CORRECT_ANIMAL, out.create(CORRECT_ANIMAL));

        verify(animalRepository, times(1)).save(CORRECT_ANIMAL);
    }

    @Test
    @DisplayName("Получение животного по ID владельца")
    void shouldReturnAnimalListByOwnerId() {
        when(animalRepository.getAllByOwnerId(CORRECT_OWNER_ID))
                .thenReturn(List.of(CORRECT_ANIMAL));

        assertEquals(List.of(CORRECT_ANIMAL), out.getAllByUserId(CORRECT_OWNER_ID));

        verify(animalRepository, times(1)).getAllByOwnerId(CORRECT_OWNER_ID);
    }


    @Test
    @DisplayName("Получение списка всех животных")
    void shouldReturnCorrectListOfAllAnimal() {
        when(animalRepository.findAll())
                .thenReturn(List.of(CORRECT_ANIMAL));

        assertEquals(List.of(CORRECT_ANIMAL), out.getAll());

        verify(animalRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Проверка свободных животных")
    void shouldReturnListOfFreeAnimal() {
        when(animalRepository.getAllByOwnerId(null))
                .thenReturn(List.of(FREE_ANIMAL));

        assertEquals(List.of(FREE_ANIMAL), out.freeAnimalList());

        verify(animalRepository, times(1)).getAllByOwnerId(null);
    }

        @Test
    @DisplayName("Получение обновленного животного")
    void shouldUpdateAnimal() {
        when(animalRepository.findById(CORRECT_ID))
                .thenReturn(Optional.of(CORRECT_ANIMAL));

        when(animalRepository.save(CORRECT_ANIMAL))
                .thenReturn(CORRECT_ANIMAL);

        Animal result = out.update(CORRECT_ANIMAL);

        assertEquals(CORRECT_ANIMAL, result);

        verify(animalRepository, times(1)).findById(CORRECT_ID);

        verify(animalRepository, times(1)).save(CORRECT_ANIMAL);
    }


    @Test
    @DisplayName("Удаление животного по ID")
    void shouldDeleteAnimalById() {
        when(animalRepository.findById(CORRECT_ID))
                .thenReturn(Optional.of(CORRECT_ANIMAL));

        doNothing().when(animalRepository).delete(CORRECT_ANIMAL);

        assertEquals("Животное удалено", out.delete(CORRECT_ID));

        verify(animalRepository, times(1)).findById(CORRECT_ID);
    }

    @Test
    @DisplayName("Выброс исключения, если список животных пустой")
    void shouldThrowNotFoundExceptionIfAnimalListIsEmpty(){
        when(animalRepository.getAllByOwnerId(CORRECT_OWNER_ID))
                .thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> out.getAllByUserId(CORRECT_OWNER_ID));
    }

    @Test
    @DisplayName("Получение списка однотипных животных")
    void shouldReturnAnimalsByType(){
        when(animalRepository.getAnimalsByAnimalType(CORRECT_TYPE))
                .thenReturn(List.of(CORRECT_ANIMAL));

        assertEquals(List.of(CORRECT_ANIMAL), out.getAnimalsByType(CORRECT_TYPE));

        verify(animalRepository, times(1)).getAnimalsByAnimalType(CORRECT_TYPE);
    }

    @Test
    @DisplayName("Корректная настройка владельца")
    void shouldReturnCorrectAnimalWhenSetOwner(){
        when(animalRepository.findById(CORRECT_ID))
                .thenReturn(Optional.of(CORRECT_ANIMAL_WITHOUT_OWNER));

        assertEquals(CORRECT_ANIMAL, out.setOwner(CORRECT_ID, CORRECT_OWNER));

        verify(animalRepository, times(1)).save(CORRECT_ANIMAL);
    }
}