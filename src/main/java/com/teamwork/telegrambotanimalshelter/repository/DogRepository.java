package com.teamwork.telegrambotanimalshelter.repository;

import com.teamwork.telegrambotanimalshelter.model.animals.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DogRepository extends JpaRepository <Dog, Long> {
    List<Dog> findAllByOwnerId(Long id);
}
