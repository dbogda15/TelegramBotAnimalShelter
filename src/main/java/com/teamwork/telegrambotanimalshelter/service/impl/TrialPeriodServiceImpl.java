package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.repository.TrialPeriodRepository;
import com.teamwork.telegrambotanimalshelter.service.TrialPeriodService;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class TrialPeriodServiceImpl implements TrialPeriodService {
    private final TrialPeriodRepository trialPeriodRepository;

    public TrialPeriodServiceImpl(TrialPeriodRepository trialPeriodRepository) {
        this.trialPeriodRepository = trialPeriodRepository;
    }

    @Override
    public TrialPeriod create(TrialPeriod trialPeriod) {
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
    public String deleteById(Long id) {
        Optional<TrialPeriod> trialPeriod = trialPeriodRepository.findById(id);
        String message;
        if(trialPeriod.isPresent()){
            trialPeriodRepository.delete(trialPeriod.get());
            message = "Испытательный срок удалён!";
        }
        else throw new NotFoundException("Испытательного срока с таким ID не существует!");
        return message;
    }

    @Override
    public TrialPeriod update(TrialPeriod trialPeriod) {
        return trialPeriodRepository.save(trialPeriod);
    }
}
