package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.repository.AnimalRepository;
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
    @Mock
    AnimalRepository animalRepository;

    @InjectMocks
    AnimalServiceImpl out;
    public static final Long CORRECT_ID = 1L;
    public static final String CORRECT_NAME = "Cat";
    public static final Long SHELTER_ID = 1L;
    public static final String NEW_NAME = "Kitty";
    public static final Integer CORRECT_AGE = 1;
    public static final AnimalType CORRECT_TYPE = AnimalType.CAT;
    public static final Long CORRECT_OWNER_ID = 2L;
    public static final String CORRECT_OWNER_PHONE = "89991234567";
    public static final String CORRECT_OWNER_NAME = "Name";
    public static final Owner CORRECT_OWNER = new Owner(CORRECT_OWNER_NAME, CORRECT_OWNER_PHONE);
    public static final Animal CORRECT_ANIMAL = new Animal(CORRECT_ID, CORRECT_TYPE, CORRECT_NAME, CORRECT_AGE, CORRECT_OWNER, SHELTER_ID);
    public static final Animal FREE_ANIMAL = new Animal(CORRECT_ID, CORRECT_TYPE, CORRECT_NAME, CORRECT_AGE, null, null);
    public static final Animal UPDATED_ANIMAL = new Animal(CORRECT_ID, CORRECT_TYPE, NEW_NAME, CORRECT_AGE, CORRECT_OWNER, SHELTER_ID);

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
    void shouldThrowExceptionWhenRepositoryThrowsException() {
        when(animalRepository.findById(any()))
                .thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> out.getById(CORRECT_ID));
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
    @DisplayName("Выброс исключения, если животное не существует")
    void shouldThrowNotFoundExceptionWhenAnimalDoesntExists() {
        when(animalRepository.findById(CORRECT_ID))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> out.getById(CORRECT_ID));

        verify(animalRepository, times(1)).findById(CORRECT_ID);
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
    @DisplayName("Выброс исключения, если владельца не существует")
    void shouldThrowNotFoundExceptionWhenOwnerDoesntExists() {
        when(animalRepository.getAllByOwnerId(CORRECT_OWNER_ID))
                .thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> out.getAllByUserId(CORRECT_OWNER_ID));

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
    @DisplayName("Выброс исключения, если удаляемого животного не существует")
    void shouldThrowNotFoundExceptionWhenDeleteAnimal() {
        when(animalRepository.findById(CORRECT_ID))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> out.delete(CORRECT_ID));

        verify(animalRepository, times(1)).findById(CORRECT_ID);
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
                .thenReturn(Optional.of(UPDATED_ANIMAL));
        when(animalRepository.save(UPDATED_ANIMAL))
                .thenReturn(UPDATED_ANIMAL);

        assertEquals(UPDATED_ANIMAL, out.getById(CORRECT_ID));
        assertEquals(UPDATED_ANIMAL, out.update(UPDATED_ANIMAL));


        verify(animalRepository, times(2)).findById(CORRECT_ID);
        verify(animalRepository, times(1)).save(UPDATED_ANIMAL);
    }

    @Test
    @DisplayName("Выбрасывает исключение, если была попытка настройки несуществующего животного")
    void shouldThrowNotFoundExceptionAfterSettingOwner() {
        when(animalRepository.findById(CORRECT_ID))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> out.getById(CORRECT_ID));

        verify(animalRepository, times(1)).findById(CORRECT_ID);
    }

}