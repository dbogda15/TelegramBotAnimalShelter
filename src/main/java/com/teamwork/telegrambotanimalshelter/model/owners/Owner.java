package com.teamwork.telegrambotanimalshelter.model.owners;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Класс владельцев
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Owner {
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
     * Имя владельца животного
     */
    private String name;
    /**
     * Номер телефона владельца животного
     */
    private String phone;
    /**
     *
     */
    private AnimalType ownerType;
    /**
     * Животное, принадлежащее данному владельцу
     */
    @JsonIgnoreProperties(value = "owner", allowGetters = true)
    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Animal> animals;
    /**
     * Список испытательных сроков
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ownerId")
    private List<TrialPeriod> trialPeriodList;
    public Owner(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
    public Owner(Long chatId, String name, List<Animal> animals){
        this.chatId = chatId;
        this.name = name;
        this.animals = animals;
    }
    public Owner(Long chatId, String name){
        this.chatId = chatId;
        this.name = name;
    }
    public Owner(Long chatId, String name, String phone, List<TrialPeriod> trialPeriodList) {
        this.chatId = chatId;
        this.name = name;
        this.phone = phone;
        this.trialPeriodList = trialPeriodList;
    }

    public Owner(Long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public Owner(Long id, Long chatId, String name, String phone) {
        this.id = id;
        this.chatId = chatId;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "id = " + id + ", name= " + name + ", phone='" + phone +
                ", ownerType = " + ownerType + ", animal = " + animals.toString();
    }
}
