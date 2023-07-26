package com.teamwork.telegrambotanimalshelter.model.animals;

import jakarta.persistence.*;

/**
 * Класс для хранения информации о состоянии собаки
 */
@Entity
@Table(name = "dog")
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
    public Dog(){
    }

    /**
     * Конструктор для создания объекта собаки, которая не имеет хозяина
     * @param name, age
     * @param age, age
     */
    public Dog(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
    public Dog(Long id, String name, Integer age, Long ownerId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
