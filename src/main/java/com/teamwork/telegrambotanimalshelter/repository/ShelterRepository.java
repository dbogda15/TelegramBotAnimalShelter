package com.teamwork.telegrambotanimalshelter.repository;

import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.shelters.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterRepository extends JpaRepository<Shelter,Long> {

    Shelter getShelterByShelterType(AnimalType type);
}
