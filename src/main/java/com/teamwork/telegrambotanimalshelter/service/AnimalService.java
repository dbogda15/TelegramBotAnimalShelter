package com.teamwork.telegrambotanimalshelter.service;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import org.springframework.stereotype.Service;

import java.util.List;
public interface AnimalService {
    /**
     * Создание записи о новом животном в БД
     * @param Animal
     * @return Новое животное
     */
    Animal create(Animal Animal);

    /**
     * Получение информации о животном по его идентификатору
     * @param id
     * @return Объект Animal
     */
    Animal getById(Long id);

    /**
     * Получение списка животных владельца по его идентифиувтору
     * @param id
     * @return Список животных
     */
    List<Animal> getAllByUserId(Long id);

    /**
     * Обновление информации о животном
     * @param Animal
     * @return
     */
    Animal update(Animal Animal);

    /**
     * Получение списка всех животных, зарегистрированных в БД
     * @return Список всех животных
     */
    List<Animal> getAll();

    /**
     * Удаление животного из БД по его идентификатору
     * @param id
     */
    void delete(Long id);

    /**
     * Получение списка свободных животных
     * @return
     */
    List<Animal> freeAnimalList();

    /**
     * Назначение животному нового хозяина/ Изменение данных о хозяине животного
     * @param id
     * @param owner
     * @return
     */
    Animal setOwner(Long id, Long ownerId);

    /**
     * Получение списка животных по конкретному типу
     * @param animalType
     * @return
     */
    List<Animal> getAnimalsByType(AnimalType animalType);

    /**
     * Назначение животному ID приюта
     * @param animalId
     * @param shelterId
     * @return
     */
    Animal setShelterId(Long animalId, Long shelterId);
}
