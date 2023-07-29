package com.teamwork.telegrambotanimalshelter.service;

import com.teamwork.telegrambotanimalshelter.exceptions.IncorrectArgumentException;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OwnerService {
    Owner create(Owner owner, Long animalId) throws IncorrectArgumentException;
    Owner getById(Long id);
    Owner update(Owner owner);
    List<Animal> getAnimalByOwnerId(Long id);
    void delete(Long id);
    List<Owner> getAll();
}
