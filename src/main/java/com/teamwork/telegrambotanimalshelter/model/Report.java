package com.teamwork.telegrambotanimalshelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "photo_id")
    private String photoId;
    @Column(name = "food_ration")
    private String foodRation;
    @Column(name = "state_of_health")
    private String stateOfHealth;
    @Column(name = "change_in_behavior")
    private String changeInBehavior;
    @Column(name = "date_of_report")
    private LocalDate dateOfReport;
    @Column(name = "trial_period_id")
    private Long trialPeriodId;

    public Report(String photoId, String foodRation, String stateOfHealth, String changeInBehavior, LocalDate dateOfReport, Long trialPeriodId) {
        this.photoId = photoId;
        this.foodRation = foodRation;
        this.stateOfHealth = stateOfHealth;
        this.changeInBehavior = changeInBehavior;
        this.dateOfReport = dateOfReport;
        this.trialPeriodId = trialPeriodId;
    }
}
