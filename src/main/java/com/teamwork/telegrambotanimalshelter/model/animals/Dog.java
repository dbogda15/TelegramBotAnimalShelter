package com.teamwork.telegrambotanimalshelter.model.animals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс для хранения информации о состоянии собаки
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Dog {
    /**
     * Идентификатор собаки для работы в БД
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Кличка собаки
     */
    private String name;
    /**
     * Возраст собаки. В полных годах
     */
    private Integer age;
    /**
     * Идентификатор владельца собаки для связи с ним
     */
    @Column(name = "owner_id")
    private Long ownerId;

    @Override
    public String toString() {
        return "Dog id = " + id +
                ", name: " + name +
                ", age = " + age;
    }
}
