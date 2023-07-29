package com.teamwork.telegrambotanimalshelter.model.animals;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    public Animal(AnimalType animalType, String name, Integer age) {
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

    @Override
    public String toString() {
        return "Animal id = " + id +
                ", name: " + name +
                ", age = " + age;
    }
}
