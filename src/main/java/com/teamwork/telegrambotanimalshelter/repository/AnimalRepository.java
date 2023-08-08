package com.teamwork.telegrambotanimalshelter.repository;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalRepository extends JpaRepository <Animal, Long> {
    List<Animal> getAllByOwnerId(Long id);
    List<Animal> getAnimalsByShelterId(Long id);
    List<Animal> getAnimalsByAnimalType(AnimalType animalType);
}
