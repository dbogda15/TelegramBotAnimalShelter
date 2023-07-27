package com.teamwork.telegrambotanimalshelter.model.shelters;

import javax.persistence.*;

/**
 * Класс для хранения полезной информации о приюте для собак
 */
@Entity
@Table(name = "dog_shelter")
public class DogShelter {
    /**
     * Идентификатор приюта для работы в БД
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Название приюта для собак
     */
    private String name;
    /**
     * Адрес приюта для собак
     */
    private String address;
    /**
     * График работы приюта
     */
    private String timetable;
    /**
     * Информация об охране (Имя, номер телефона)
     */
    @Column(name = "guards_info")
    private String guardsInfo;
    /**
     * Рекомендуемые правила безопасности нахождения на территории приюта
     */
    @Column(name = "safety_regulations")
    private String safetyRegulations;

    public DogShelter(){
    }
    public DogShelter(String name, String address, String timetable, String guardsInfo, String safetyRegulations) {
        this.name = name;
        this.address = address;
        this.timetable = timetable;
        this.guardsInfo = guardsInfo;
        this.safetyRegulations = safetyRegulations;
    }

    public DogShelter(Long id, String name, String address, String timetable, String guardsInfo, String safetyRegulations) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.timetable = timetable;
        this.guardsInfo = guardsInfo;
        this.safetyRegulations = safetyRegulations;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimetable() {
        return timetable;
    }

    public void setTimetable(String timetable) {
        this.timetable = timetable;
    }

    public String getGuardsInfo() {
        return guardsInfo;
    }

    public void setGuardsInfo(String guardsInfo) {
        this.guardsInfo = guardsInfo;
    }

    public String getSafetyRegulations() {
        return safetyRegulations;
    }

    public void setSafetyRegulations(String safetyRegulations) {
        this.safetyRegulations = safetyRegulations;
    }
}
