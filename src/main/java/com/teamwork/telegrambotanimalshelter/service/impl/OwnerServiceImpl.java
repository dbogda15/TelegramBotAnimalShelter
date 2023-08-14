package com.teamwork.telegrambotanimalshelter.service.impl;

import com.teamwork.telegrambotanimalshelter.exceptions.IncorrectArgumentException;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.repository.OwnerRepository;
import com.teamwork.telegrambotanimalshelter.service.AnimalService;
import com.teamwork.telegrambotanimalshelter.service.OwnerService;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final AnimalService animalService;

    public OwnerServiceImpl(OwnerRepository ownerRepository, AnimalService animalService) {
        this.ownerRepository = ownerRepository;
        this.animalService = animalService;
    }

    @Override
    public Owner create(Owner owner, Long animalId) {
        if (animalService.getById(animalId).getOwner() != null) {
            throw new IncorrectArgumentException("У этого животного есть хозяин!");
        }
        AnimalType animalType = animalService.getById(animalId).getAnimalType();
        owner.setOwnerType(animalType);
        animalService.getById(animalId).setOwner(owner);
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
        String message;
        Optional<Owner> owner = ownerRepository.findById(id);
        if(owner.isPresent()){
            ownerRepository.delete(owner.get());
            message = "Владелец удален";
        }
        else throw new NotFoundException("Владельца с таким ID не существует!");
        return message;
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
