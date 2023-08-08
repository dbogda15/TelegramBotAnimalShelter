package com.teamwork.telegrambotanimalshelter.service;

import com.teamwork.telegrambotanimalshelter.model.shelters.Shelter;

public interface ShelterService {

    Shelter create(Shelter shelter);

    Shelter getById(Long id);

    void delete(Long id);

    Shelter update(Shelter shelter);
}
