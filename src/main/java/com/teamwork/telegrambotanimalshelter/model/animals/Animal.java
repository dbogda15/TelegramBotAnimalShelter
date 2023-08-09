package com.teamwork.telegrambotanimalshelter.model.animals;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.teamwork.telegrambotanimalshelter.exceptions.IncorrectArgumentException;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс для хранения информации о животном
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Animal {
    /**
     * Идентификатор животного для работы в БД
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Enum для определения типа животного
     */
    private AnimalType animalType;
    /**
     * Кличка животного
     */
    private String name;
    /**
     * Возраст животного. В полных годах
     */
    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "owner")
    private Owner owner;
    private Long shelterId;
    public Animal(AnimalType animalType, String name, Integer age) {
        this.animalType = animalType;
        this.name = name;
        this.age = age;
    }

    public Animal(Long id, AnimalType animalType, String name, Integer age) {
        this.id = id;
        this.animalType = animalType;
        this.name = name;
        this.age = age;
    }

    public Animal(AnimalType animalType, String name, Integer age, Owner owner) {
        this.animalType = animalType;
        this.name = name;
        this.age = age;
        this.owner = owner;
    }

    public void setOwner(Owner owner) {
        if(owner.getOwnerType().equals(this.getAnimalType())) {
            this.owner = owner;
        } else throw new IncorrectArgumentException("У одного владельца могут быть только однотипные животные");
    }
    @Override
    public String toString() {
        return "Animal id = " + id +
                ", name: " + name +
                ", age = " + age;
    }
}
