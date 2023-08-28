package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.exceptions.IncorrectArgumentException;
import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.enums.TrialPeriodType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.repository.OwnerRepository;
import com.teamwork.telegrambotanimalshelter.service.AnimalService;
import com.teamwork.telegrambotanimalshelter.service.OwnerService;
import com.teamwork.telegrambotanimalshelter.service.TrialPeriodService;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final AnimalService animalService;
    private final TrialPeriodService trialPeriodService;

    public OwnerServiceImpl(OwnerRepository ownerRepository, AnimalService animalService, TrialPeriodService trialPeriodService) {
        this.ownerRepository = ownerRepository;
        this.animalService = animalService;
        this.trialPeriodService = trialPeriodService;
    }

    @Override
    public Owner create(Long  ownerId, Long animalId) {
        if (animalService.getById(animalId).getOwner() != null) {
            throw new IncorrectArgumentException("У этого животного есть хозяин!");
        }
        trialPeriodService.create(new TrialPeriod(LocalDate.now(), LocalDate.now().plusDays(30), LocalDate.now().minusDays(1),
                        ownerId, animalId, animalService.getById(animalId).getAnimalType(), new ArrayList<>(), TrialPeriodType.IN_PROGRESS));
        AnimalType animalType = animalService.getById(animalId).getAnimalType();
        Owner owner =getById(ownerId);
        owner.setOwnerType(animalType);
        animalService.getById(animalId).setOwner(owner);
        trialPeriodService.create(new TrialPeriod(LocalDate.now(), LocalDate.now().plusDays(30), LocalDate.now().minusDays(1),
                owner.getId(), animalId, animalType, new ArrayList<>(), TrialPeriodType.IN_PROGRESS));
        return ownerRepository.save(owner);
    }

    @Override
    public Owner create(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Override
    public Owner getById(Long id) {
        Optional<Owner> owner = ownerRepository.findById(id);
        if (owner.isEmpty()){
            throw new NotFoundException("Владельца с таким ID не существует!");
        }
        return owner.get();
    }

    @Override
    public Owner update(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Override
    public List<Animal> getAnimalByOwnerId(Long id) {
        Owner owner = ownerRepository.findById(id).get();
        return owner.getAnimals();
    }

    @Override
    public String delete(Long id) {
        return ownerRepository.findById(id).map(owner -> {
            ownerRepository.delete(owner);
            return "Владелец удален";
        }).orElseThrow(() -> new NotFoundException("Владельца с таким ID не существует!"));
    }

    @Override
    public List<Owner> getAll() {
        return ownerRepository.findAll();
    }

    @Override
    public Owner getByChatId(Long chatId) {
        return ownerRepository.getOwnerByChatId(chatId);
    }
}
