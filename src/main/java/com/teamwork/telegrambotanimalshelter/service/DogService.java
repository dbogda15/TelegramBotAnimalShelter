package com.teamwork.telegrambotanimalshelter.service;

import com.teamwork.telegrambotanimalshelter.model.animals.Dog;

import java.util.List;
public interface DogService {
    Dog create(Dog dog);
    Dog getById(Long id);
    List<Dog> getAllByUserId(Long id);
    Dog update(Dog dog);
    List<Dog> getAll();
    void delete(Long id);
}
