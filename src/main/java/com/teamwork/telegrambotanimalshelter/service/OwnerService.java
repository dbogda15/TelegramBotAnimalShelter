package com.teamwork.telegrambotanimalshelter.service;

import com.teamwork.telegrambotanimalshelter.exceptions.IncorrectArgumentException;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OwnerService {
    /**
     * Создание и добавдение в БД нового владельца
     * @param ownerId
     * @param animalId
     * @return Owner
     * @throws IncorrectArgumentException
     */
    Owner create(Long ownerId, Long animalId) throws IncorrectArgumentException;

    Owner create(Owner owner);
    Owner getByChatId(Long chatId);
    /**
     * Получение владельца животного по его ID
     * @param id
     * @return Owner
     */
    Owner getById(Long id);

    /**
     * Обновление данных в БД о сущесвтующем владельце
     * @param owner
     * @return
     */
    Owner update(Owner owner);

    /**
     * Получение списка животных, закрепленных за владельцем по его ID
     * @param id
     * @return
     */
    List<Animal> getAnimalByOwnerId(Long id);

    /**
     * Удвление владельца (Owner) из БД по его ID
     * @param id
     */
    String delete(Long id);

    /**
     * Получение списка всех владельцев
     * @return
     */
    List<Owner> getAll();
}
