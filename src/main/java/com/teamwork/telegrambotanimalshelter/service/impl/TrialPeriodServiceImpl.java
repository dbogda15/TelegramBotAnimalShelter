package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.repository.TrialPeriodRepository;
import com.teamwork.telegrambotanimalshelter.service.AnimalService;
import com.teamwork.telegrambotanimalshelter.service.TrialPeriodService;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class TrialPeriodServiceImpl implements TrialPeriodService {
    private final TrialPeriodRepository trialPeriodRepository;
    private final AnimalService animalService;

    public TrialPeriodServiceImpl(TrialPeriodRepository trialPeriodRepository, AnimalService animalService) {
        this.trialPeriodRepository = trialPeriodRepository;
        this.animalService = animalService;
    }

    @Override
    public TrialPeriod create(TrialPeriod trialPeriod) {
        if (trialPeriod.getAnimalType() == null) {
            Long animalId = trialPeriod.getAnimalId();
            AnimalType animalType = animalService.getById(animalId).getAnimalType();
            trialPeriod.setAnimalType(animalType);
            return trialPeriodRepository.save(trialPeriod);
        }
        return trialPeriodRepository.save(trialPeriod);
    }

    @Override
    public TrialPeriod findById(Long id) {
        Optional<TrialPeriod> trialPeriod = trialPeriodRepository.findById(id);
        if (trialPeriod.isEmpty()) {
            throw new NotFoundException("Испытательного срока с таким ID не существует");
        }
        return trialPeriod.get();
    }

    @Override
    public void delete(TrialPeriod trialPeriod) {
        trialPeriodRepository.delete(findById(trialPeriod.getId()));
    }

    @Override
    public List<TrialPeriod> findAll() {
        List<TrialPeriod> trialPeriods = trialPeriodRepository.findAll();
        if(trialPeriods.isEmpty()){
            throw new NotFoundException("У данного владельца нет испытательных сроков");
        }
        return trialPeriods;
    }

    @Override
    public List<TrialPeriod> findAllByOwnerId(Long id) {
       List<TrialPeriod> trialPeriods = trialPeriodRepository.findAllByOwnerId(id);
       if(trialPeriods.isEmpty()){
           throw new NotFoundException("У данного владельца нет испытательных сроков");
       }
       return trialPeriods;
    }

    @Override
    public void delete(Long id) {
        trialPeriodRepository.deleteById(id);
    }

    @Override
    public TrialPeriod update(TrialPeriod trialPeriod) {
        return trialPeriodRepository.save(trialPeriod);
    }
}
