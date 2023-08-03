package com.teamwork.telegrambotanimalshelter.service.impl;

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

import static com.teamwork.telegrambotanimalshelter.constants.AnimalServiceImplConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceImplTest {
    @Mock
   AnimalRepository animalRepository;

    @InjectMocks
    AnimalServiceImpl out;

    @Test
    @DisplayName("Получение животного по его ID")
    void shouldReturnCorrectAnimalIfIdIsCorrect(){
        when(animalRepository.findById(CORRECT_ID))
                .thenReturn(Optional.of(CORRECT_ANIMAL));

        assertEquals(CORRECT_ANIMAL, out.getById(CORRECT_ID));

        verify(animalRepository, times(1)).findById(CORRECT_ID);
    }

    @Test
    @DisplayName("Выброс нового исключения из репозитория")
    void shouldThrowExceptionWhenRepositoryThrowsException(){
        when(animalRepository.findById(any()))
                .thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> out.getById(CORRECT_ID));
    }

    @Test
    @DisplayName("Проверка сохранения нового животного")
    void shouldReturnCorrectAnimalWhenCreate(){
        when(animalRepository.save(CORRECT_ANIMAL))
                .thenReturn(CORRECT_ANIMAL);

        assertEquals(CORRECT_ANIMAL, out.create(CORRECT_ANIMAL));

        verify(animalRepository, times(1)).save(CORRECT_ANIMAL);
    }

    @Test
    @DisplayName("Выброс исключения, если животное не существует")
    void shouldThrowNotFoundExceptionWhenAnimalDoesntExists(){
        when(animalRepository.findById(CORRECT_ID))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> out.getById(CORRECT_ID));

        verify(animalRepository, times(1)).findById(CORRECT_ID);
    }

    @Test
    @DisplayName("Получение животного по ID владельца")
    void shouldReturnAnimalListByOwnerId(){
        when(animalRepository.getAllByOwnerId(CORRECT_OWNER_ID))
                .thenReturn(List.of(CORRECT_ANIMAL));

        assertEquals(List.of(CORRECT_ANIMAL), out.getAllByUserId(CORRECT_OWNER_ID));

        verify(animalRepository, times(1)).getAllByOwnerId(CORRECT_OWNER_ID);
    }

    @Test
    @DisplayName("Выброс исключения, если владельца не существует")
    void shouldThrowNotFoundExceptionWhenOwnerDoesntExists(){
        when(animalRepository.getAllByOwnerId(CORRECT_OWNER_ID))
                .thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> out.getAllByUserId(CORRECT_OWNER_ID));

        verify(animalRepository, times(1)).getAllByOwnerId(CORRECT_OWNER_ID);
    }

    @Test
    @DisplayName("Получение списка всех животных")
    void shouldReturnCorrectListOfAllAnimal(){
        when(animalRepository.findAll())
                .thenReturn(List.of(CORRECT_ANIMAL));

        assertEquals(List.of(CORRECT_ANIMAL), out.getAll());

        verify(animalRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Выброс исключения, если удаляемого животного не существует")
    void shouldThrowNotFoundExceptionWhenDeleteAnimal(){
        when(animalRepository.findById(CORRECT_ID))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> out.delete(CORRECT_ID));

        verify(animalRepository, times(1)).findById(CORRECT_ID);
    }

    @Test
    void shouldReturnListOfFreeAnimal(){
        when(animalRepository.getAllByOwnerId(null))
                .thenReturn(List.of(FREE_ANIMAL));

        assertEquals(List.of(FREE_ANIMAL), out.freeAnimalList());

        verify(animalRepository, times(1)).getAllByOwnerId(null);
    }

    //    @Test
//    @DisplayName("Получение обновленного животного")
//    void shouldUpdateCorrectAnimal() {
//
//    }

    //    @Test
//    @DisplayName("Возвращает животное, если настройка владельца прошла успешно")
//    void shouldReturnCorrectAnimalAfterSettingOwner(){
//        when(animalRepository.findById(CORRECT_ID))
//                .thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> out.getById(CORRECT_ID));
//
//        verify(animalRepository, times(1)).findById(CORRECT_ID);
//    }

}