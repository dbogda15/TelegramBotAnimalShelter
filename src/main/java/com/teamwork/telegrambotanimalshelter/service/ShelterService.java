package com.teamwork.telegrambotanimalshelter.service;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.shelters.Shelter;
import io.swagger.v3.oas.models.links.Link;

import java.util.List;

public interface ShelterService {

    Shelter create(Shelter shelter);

    Shelter getById(Long id);

    void delete(Long id);

    Shelter update(Shelter shelter);
    List<Animal> getAnimals(Long shelterId);
}
