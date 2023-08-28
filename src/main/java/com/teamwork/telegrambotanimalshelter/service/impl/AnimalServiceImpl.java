package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.exceptions.IncorrectArgumentException;
import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.enums.TrialPeriodType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.model.shelters.Shelter;
import com.teamwork.telegrambotanimalshelter.repository.AnimalRepository;
import com.teamwork.telegrambotanimalshelter.service.AnimalService;
import com.teamwork.telegrambotanimalshelter.service.ShelterService;
import com.teamwork.telegrambotanimalshelter.service.TrialPeriodService;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.*;
@Service
public class AnimalServiceImpl implements AnimalService {
    private final AnimalRepository animalRepository;
    private final ShelterService shelterService;
    private final TrialPeriodService trialPeriodService;
    public AnimalServiceImpl(AnimalRepository animalRepository, ShelterService shelterService, TrialPeriodService trialPeriodService) {
        this.animalRepository = animalRepository;
        this.shelterService = shelterService;
        this.trialPeriodService = trialPeriodService;
    }

    @Override
    public Animal create(Animal animal) {
        animalRepository.save(animal);
        return animal;
    }

    @Override
    public Animal getById(Long id) {
        Optional<Animal> optionalAnimal = animalRepository.findById(id);
        if (optionalAnimal.isEmpty()){
            throw new NotFoundException("Животного с таким ID не существует");
        }
        return optionalAnimal.get();
    }

    @Override
    public List<Animal> getAllByUserId(Long id) {
        List<Animal> result = animalRepository.getAllByOwnerId(id);
        if (result.isEmpty()){
            throw new NotFoundException("У пользователя нет животных");
        }
        return result;
    }

    @Override
    public Animal update(Animal animal) {
        Animal existAnimal = getById(animal.getId());
        return animalRepository.save(existAnimal);
    }

    @Override
    public Animal setOwner(Long id, Owner owner) {
        Animal existAnimal = getById(id);
        if (existAnimal.getOwner() != null) {
            throw new IncorrectArgumentException("У этого животного есть хозяин!");
        }
        existAnimal.setOwner(owner);
        owner.setOwnerType(existAnimal.getAnimalType());
        trialPeriodService.create(new TrialPeriod(LocalDate.now(), LocalDate.now().plusDays(30), LocalDate.now().minusDays(1),
                owner.getId(), id, existAnimal.getAnimalType(), new ArrayList<>(), TrialPeriodType.IN_PROGRESS));
        return animalRepository.save(existAnimal);
    }

    @Override
    public List<Animal> getAll() {
        return animalRepository.findAll();
    }


    @Override
    public String delete(Long id) {
        return animalRepository.findById(id).map(animal -> {
            animalRepository.delete(animal);
            return "Животное удалено";
        }).orElseThrow(() -> new NotFoundException("Животное с таким ID не существует"));
    }

    @Override
    public List<Animal> freeAnimalList() {
        return animalRepository.getAllByOwnerId(null);
    }

    @Override
    public List<Animal> getAnimalsByType(AnimalType animalType) {
        return animalRepository.getAnimalsByAnimalType(animalType);
    }

    @Override
    public Animal setShelterId(Long animalId, Long shelterId) {
        Animal animal = getById(animalId);
        Shelter shelter = shelterService.getById(shelterId);
        if (animal.getAnimalType().equals(shelter.getShelterType())){
            animal.setShelterId(shelterId);
        } else throw new IncorrectArgumentException("Вы не можете добавить это животное в приют, так как они разнотипные!");
        return animalRepository.save(animal);
    }
}
