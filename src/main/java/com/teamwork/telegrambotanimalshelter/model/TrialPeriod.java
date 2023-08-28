package com.teamwork.telegrambotanimalshelter.model;

import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.enums.TrialPeriodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TrialPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date_of_the_start")
    private LocalDate dateOfTheStart;
    @Column(name = "date_of_the_end")
    private LocalDate dateOfTheEnd;
    @Column(name = "last_date_of_the_report")
    private LocalDate lastDateOfReport;
    @Column(name = "owner_id")
    private Long ownerId;
    @Column(name = "animal_id")
    private Long animalId;
    @Column(name = "animal_type")
    @Enumerated(value = EnumType.STRING)
    private AnimalType animalType;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "trialPeriodId")
    private List<Report> reports;
    @Column(name = "trial_period_type")
    @Enumerated(value = EnumType.STRING)
    private TrialPeriodType periodType;

    public TrialPeriod(LocalDate dateOfTheStart, LocalDate dateOfTheEnd, LocalDate lastDateOfReport, Long ownerId, Long animalId, List<Report> reports) {
        this.dateOfTheStart = dateOfTheStart;
        this.dateOfTheEnd = dateOfTheEnd;
        this.lastDateOfReport = lastDateOfReport;
        this.ownerId = ownerId;
        this.animalId = animalId;
        this.reports = reports;
    }

    public TrialPeriod(LocalDate dateOfTheStart, LocalDate dateOfTheEnd, Long ownerId, Long animalId, AnimalType animalType, List<Report> reports, TrialPeriodType periodType) {
        this.dateOfTheStart = dateOfTheStart;
        this.dateOfTheEnd = dateOfTheEnd;
        this.ownerId = ownerId;
        this.animalId = animalId;
        this.animalType = animalType;
        this.reports = reports;
        this.periodType = periodType;
    }

    public TrialPeriod(LocalDate dateOfTheStart, LocalDate dateOfTheEnd, LocalDate lastDateOfReport, Long ownerId, Long animalId, AnimalType animalType, List<Report> reports, TrialPeriodType periodType) {
        this.dateOfTheStart = dateOfTheStart;
        this.dateOfTheEnd = dateOfTheEnd;
        this.lastDateOfReport = lastDateOfReport;
        this.ownerId = ownerId;
        this.animalId = animalId;
        this.animalType = animalType;
        this.reports = reports;
        this.periodType = periodType;
    }
}
