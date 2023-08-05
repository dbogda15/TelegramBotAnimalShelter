package com.teamwork.telegrambotanimalshelter.model;

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
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "date_of_the_start")
    private LocalDate dateOfTheStart;
    @Column(name = "date_of_the_end")
    private LocalDate dateOfTheEnd;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "")
    private List<Report> reports;
    private TrialPeriodType type;
}
