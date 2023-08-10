package com.teamwork.telegrambotanimalshelter.constants;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;

public class AnimalServiceImplConstants {

    public static final Long CORRECT_ID = 1L;
    public static final String CORRECT_NAME = "Cat";
    public static final String NEW_NAME = "Kitty";
    public static final Integer CORRECT_AGE = 1;
    public static final AnimalType CORRECT_TYPE = AnimalType.CAT;
    public static final Long CORRECT_OWNER_ID = 2L;
    public static final String CORRECT_OWNER_PHONE = "89991234567";
    public static final String CORRECT_OWNER_NAME = "Name";
    public static final Owner CORRECT_OWNER = new Owner(CORRECT_OWNER_NAME, CORRECT_OWNER_PHONE);
//    public static final Animal CORRECT_ANIMAL = new Animal(CORRECT_ID, CORRECT_TYPE, CORRECT_NAME, CORRECT_AGE, CORRECT_OWNER);
//    public static final Animal FREE_ANIMAL = new Animal(CORRECT_ID, CORRECT_TYPE, CORRECT_NAME, CORRECT_AGE, null);
//    public static final Animal UPDATED_ANIMAL = new Animal(CORRECT_ID, CORRECT_TYPE, NEW_NAME, CORRECT_AGE, CORRECT_OWNER);
}
