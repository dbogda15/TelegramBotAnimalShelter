package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.shelters.Shelter;
import com.teamwork.telegrambotanimalshelter.repository.AnimalRepository;
import com.teamwork.telegrambotanimalshelter.repository.ShelterRepository;
import com.teamwork.telegrambotanimalshelter.service.ShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShelterServiceImpl implements ShelterService {

private final ShelterRepository shelterRepository;
private final AnimalRepository animalRepository;
    @Override
    public Shelter create(Shelter shelter) {
        shelterRepository.save(shelter);
        return shelter;
    }

    @Override
    public Shelter getById(Long id) {
        Optional<Shelter> shelter = shelterRepository.findById(id);
        if(shelter.isEmpty()){
            throw new NotFoundException("Приют не найден");
        }
        return shelter.get();
    }

    @Override
    public void delete(Long id) {
        Shelter shelter = getById(id);
        shelterRepository.delete(shelter);
    }

    @Override
    public Shelter update(Shelter shelter) {
        Optional<Shelter> optional = shelterRepository.findById(shelter.getId());
        if(optional.isEmpty()){
            throw new NotFoundException("Приют не найден");
        }
        Shelter updated = optional.get();
        return shelterRepository.save(updated);
    }

    @Override
    public List<Animal> getAnimals(Long shelterId) {
        return animalRepository.getAnimalsByShelterId(shelterId);
    }

    @Override
    public Shelter getByShelterType(AnimalType type) {
        return shelterRepository.getShelterByShelterType(type);
    }
}
