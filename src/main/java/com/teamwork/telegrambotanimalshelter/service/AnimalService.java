package com.teamwork.telegrambotanimalshelter.service;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import org.springframework.stereotype.Service;

import java.util.List;
public interface AnimalService {
    Animal create(Animal Animal);
    Animal getById(Long id);
    List<Animal> getAllByUserId(Long id);
    Animal update(Animal Animal);
    List<Animal> getAll();
    void delete(Long id);
    List<Animal> freeAnimalList();
    Animal setOwner(Long id, Owner owner);
}
