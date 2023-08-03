package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.repository.AnimalRepository;
import com.teamwork.telegrambotanimalshelter.service.AnimalService;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.*;
@Service
public class AnimalServiceImpl implements AnimalService {
    private final AnimalRepository animalRepository;
    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
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
        Optional<Animal> animalId = animalRepository.findById(animal.getId());
        if (animalId.isEmpty()) {
            throw new NotFoundException("Такого животного не существует!");
        }
        Animal existAnimal = animalId.get();
        return animalRepository.save(existAnimal);
    }

    @Override
    public Animal setOwner(Long id, Owner owner) {
        Optional<Animal> animal = animalRepository.findById(id);
        if (animal.isEmpty()){
           throw new NotFoundException("Такого животного не существует!");
        }
        Animal existAnimal = animal.get();
        existAnimal.setOwner(owner);
        return animalRepository.save(existAnimal);
    }

    @Override
    public List<Animal> getAll() {
        return animalRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Animal animal = getById(id);
        animalRepository.delete(animal);
    }

    @Override
    public List<Animal> freeAnimalList() {
        return animalRepository.getAllByOwnerId(null);
    }
}
