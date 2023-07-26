package com.teamwork.telegrambotanimalshelter.service;

import com.teamwork.telegrambotanimalshelter.model.animals.Dog;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface DogService {
    Dog create(Dog dog);
    Dog getById(Long id);
    List<Dog> getAllByUserId(Long id);
    Dog update(Dog dog);
    List<Dog> getAll();
    void delete(Long id);
}
