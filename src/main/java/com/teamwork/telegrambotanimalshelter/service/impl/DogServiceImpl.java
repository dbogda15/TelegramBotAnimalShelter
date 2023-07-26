package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.model.animals.Dog;
import com.teamwork.telegrambotanimalshelter.repository.DogRepository;
import com.teamwork.telegrambotanimalshelter.service.DogService;

import java.util.ArrayList;
import java.util.List;

public class DogServiceImpl implements DogService {
    private final DogRepository dogRepository;
    public DogServiceImpl(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    @Override
    public Dog create(Dog dog) {
        return dogRepository.save(dog);
    }

    @Override
    public Dog getById(Long id) {
        return dogRepository.findById(id).get();
    }

    @Override
    public List<Dog> getAllByUserId(Long id) {
        return dogRepository.findAllByOwnerId(id);
    }

    @Override
    public Dog update(Dog dog) {
        return dogRepository.save(dog);
    }

    @Override
    public List<Dog> getAll() {
        return dogRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        dogRepository.delete(getById(id));
    }
}
