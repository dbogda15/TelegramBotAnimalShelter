package com.teamwork.telegrambotanimalshelter.model.shelters;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shelter")
public class Shelter {
    /**
     * Идентификатор приюта для работы в БД
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Enum для определения типа приюта
     */
    private AnimalType shelterType;
    /**
     * Название приюта
     */
    @Column
    private String name;

    /**
     * Адрес и схема проезда
     */
    @Column
    private String location;

    /**
     * Расписание
     */
    @Column
    private String timetable;

    /**
     * О приюте
     */
    @Column(name = "about_me")
    private String aboutMe;

    /**
     * Список животных в приюте
     */
    @OneToMany(mappedBy = "shelterId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Animal> list;

    /**
     * Способ связи с охраной
     */
    @Column
    private String security;

    /**
     * Рекомендации о технике безопасности на территории приюта
     */
    @Column(name = "safety_advice")
    private String safetyAdvice;

    /**
     * Конструктор для POST в БД (с id, без List)
     */
    public Shelter(Long idShelter, String name, String location, String timetable, String aboutMe, String security, String safetyAdvice) {
        this.id = idShelter;
        this.name = name;
        this.location = location;
        this.timetable = timetable;
        this.aboutMe = aboutMe;
        this.security = security;
        this.safetyAdvice = safetyAdvice;
    }

    /**
     * Конструктор для PUT в БД (без id, без List)
     */
    public Shelter(String name, String location, String timetable, String aboutMe, String security, String safetyAdvice, AnimalType shelterType, List<Animal> list) {
        this.name = name;
        this.location = location;
        this.timetable = timetable;
        this.aboutMe = aboutMe;
        this.security = security;
        this.safetyAdvice = safetyAdvice;
        this.shelterType = shelterType;
        this.list = list;
    }
}





