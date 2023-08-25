package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.model.shelters.Shelter;
import com.teamwork.telegrambotanimalshelter.repository.AnimalRepository;
import com.teamwork.telegrambotanimalshelter.repository.ShelterRepository;
import org.checkerframework.checker.units.qual.C;
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

import static com.teamwork.telegrambotanimalshelter.service.impl.AnimalServiceImplTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShelterServiceImplTest {

    @Mock
    AnimalRepository animalRepository;
    @Mock
    ShelterRepository shelterRepository;

    @InjectMocks
    ShelterServiceImpl out;

    public static final Long CORRECT_ID = 1L;
    public static final Long SHELTER_ID = 1L;
    public static final Long CHAT_ID = 123L;
    public static final String CORRECT_NAME = "Cat";
    public static final Integer CORRECT_AGE = 1;
    public static final AnimalType CORRECT_TYPE = AnimalType.CAT;
    private static final String NAME = "name";
    private static final String UPDATE_NAME = "names";
    private static final String LOCATION = "location";
    private static final String TIMETABLE = "timetable";
    private static final String ABOUT_ME = "aboutMe";
    private static final String SECURITY = "security";
    private static final String SAFETY_ADVICE = "safetyAdvice";
    private static final AnimalType SHELTER_TYPE = AnimalType.CAT;
    private static final List<Animal> ANIMAL_LIST = new ArrayList<>();
    public static final Owner CORRECT_OWNER = new Owner(CORRECT_OWNER_ID, CHAT_ID, CORRECT_OWNER_NAME, CORRECT_OWNER_PHONE, OWNER_TYPE, new ArrayList<>(), new ArrayList<>());

    private static final Shelter CORRECT_SHELTER = new Shelter(CORRECT_ID, NAME, LOCATION, TIMETABLE, ABOUT_ME, SECURITY,
            SAFETY_ADVICE, SHELTER_TYPE, ANIMAL_LIST);
    private static final Shelter UPDATE_SHELTER = new Shelter(UPDATE_NAME, LOCATION, TIMETABLE, ABOUT_ME, SECURITY,
            SAFETY_ADVICE, SHELTER_TYPE, ANIMAL_LIST);
    public static final Animal CORRECT_ANIMAL = new Animal(CORRECT_ID, CORRECT_TYPE, CORRECT_NAME, CORRECT_AGE, CORRECT_OWNER, SHELTER_ID);


    @Test
    @DisplayName("Создание приюта")
    void shouldReturnCorrectShelterWhenCreate() {
        when(shelterRepository.save(CORRECT_SHELTER))
                .thenReturn(CORRECT_SHELTER);

        assertEquals(CORRECT_SHELTER, out.create(CORRECT_SHELTER));

        verify(shelterRepository, times(1)).save(CORRECT_SHELTER);
    }

    @Test
    @DisplayName("Получение приюта по его ID")
    void shouldReturnCorrectShelterIfIdIsCorrect() {
        when(shelterRepository.findById(CORRECT_ID))
                .thenReturn(Optional.of(CORRECT_SHELTER));

        assertEquals(CORRECT_SHELTER, out.getById(CORRECT_ID));

        verify(shelterRepository, times(1)).findById(CORRECT_ID);
    }

    @Test
    @DisplayName("Удаление приюта по его ID")
    void deleteShelterIfIdCorrect() {
        when(shelterRepository.findById(CORRECT_ID))
                .thenReturn(Optional.of(CORRECT_SHELTER));

        doNothing().when(shelterRepository).delete(CORRECT_SHELTER);

        assertEquals("Приют удалён", out.delete(CORRECT_ID));

        verify(shelterRepository, times(1)).findById(CORRECT_ID);
    }
    @Test
    @DisplayName("Выброс исключения, если искомого приюта нет")
    void shouldThrowNotFoundExceptionWhenGetById() {
        when(shelterRepository.findById(CORRECT_ID)).
                thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> out.getById(CORRECT_ID));
        verify(shelterRepository, times(1)).findById(CORRECT_ID);
    }
    @Test
    @DisplayName("Выброс исключения, если искомого приюта нет")
    void shouldThrowNotFoundExceptionWhenUpdate() {
        when(shelterRepository.findById(CORRECT_ID)).
                thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> out.update(CORRECT_SHELTER));
        verify(shelterRepository, times(1)).findById(CORRECT_ID);
    }

    @Test
    @DisplayName("Проверка сохранения нового приюта")
    void shouldReturnCorrectShelterWhenUpdate() {
        when(shelterRepository.findById(CORRECT_ID))
                .thenReturn(Optional.of(CORRECT_SHELTER));

        when(shelterRepository.save(CORRECT_SHELTER))
                .thenReturn(CORRECT_SHELTER);


        assertEquals(CORRECT_SHELTER, out.update(CORRECT_SHELTER));

        verify(shelterRepository, times(1)).findById(CORRECT_ID);
        verify(shelterRepository, times(1)).save(CORRECT_SHELTER);
    }

    @Test
    @DisplayName("Получение списка всех животных из приюта")
    void shouldReturnCorrectAnimalsIfShelterIsCorrect() {
        when(animalRepository.getAnimalsByShelterId(CORRECT_ID)).
                thenReturn(List.of(CORRECT_ANIMAL));
        assertEquals(List.of(CORRECT_ANIMAL), out.getAnimals(CORRECT_ID));

        verify(animalRepository, times(1)).getAnimalsByShelterId(CORRECT_ID);
    }
    @Test
    @DisplayName("Получение приюта по типу питомца")
    void shouldReturnCorrectShelterIfTypeIsCorrect() {
        when(shelterRepository.getShelterByShelterType(CORRECT_TYPE)).
                thenReturn(CORRECT_SHELTER);
        assertEquals(CORRECT_SHELTER, out.getByShelterType(CORRECT_TYPE));

        verify(shelterRepository, times(1)).getShelterByShelterType(CORRECT_TYPE);
    }


}
