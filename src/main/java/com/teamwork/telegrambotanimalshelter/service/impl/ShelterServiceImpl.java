package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.model.shelters.Shelter;
import com.teamwork.telegrambotanimalshelter.repository.ShelterRepository;
import com.teamwork.telegrambotanimalshelter.service.ShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShelterServiceImpl implements ShelterService {

private final ShelterRepository shelterRepository;
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
        Optional<Shelter> shelterId = shelterRepository.findById(shelter.getId());
        if(shelterId.isEmpty()){
            throw new NotFoundException("Приют не найден");
        }
        Shelter shelter1 = shelterId.get();
        return shelterRepository.save(shelter1);
    }
}
