package com.teamwork.telegrambotanimalshelter.model.enums;

public enum AnimalType {
    DOG("DOG"),
    CAT("CAT");
    private final String animalType;

    AnimalType(String animalType){
        this.animalType=animalType;
    }

    public String getAnimalType() {
        return animalType;
    }
}
