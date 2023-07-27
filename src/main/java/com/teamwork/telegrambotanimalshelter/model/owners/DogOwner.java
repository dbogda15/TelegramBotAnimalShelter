package com.teamwork.telegrambotanimalshelter.model.owners;

import com.teamwork.telegrambotanimalshelter.model.animals.Dog;

import javax.persistence.*;
import java.util.List;
/**
 * Класс владельцев собак
 */
@Entity
@Table(name = "dog_owner")
public class DogOwner {
    /**
     * Идентификатор владельца для хранения в БД
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * ID чата в Telegram для отправки сообщений
     */
    private Long chatId;
    /**
     * Имя владельца собаки
     */
    private String name;
    /**
     * Фамилия владельца собаки
     */
    private String lastName;
    /**
     * Номер телефона владельца собаки
     */
    private String phone;
    /**
     * Список собак, принадлежащих данному владельцу
     */
    @OneToMany(mappedBy = "ownerId", fetch = FetchType.EAGER)
    private List<Dog> dogList;

    public DogOwner() {
    }

    public DogOwner(Long chatId, String name, String lastName, String phone, List<Dog> dogList) {
        this.chatId = chatId;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.dogList = dogList;
    }

    public DogOwner(Long id, Long chatId, String name, String lastName, String phone, List<Dog> dogList) {
        this.id = id;
        this.chatId = chatId;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.dogList = dogList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Dog> getDogList() {
        return dogList;
    }

    public void setDogList(List<Dog> dogList) {
        this.dogList = dogList;
    }
}
