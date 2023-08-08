package com.teamwork.telegrambotanimalshelter.repository;

import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrialPeriodRepository extends JpaRepository<TrialPeriod, Long> {
    List<TrialPeriod> findAllByOwnerId(Long id);
}
