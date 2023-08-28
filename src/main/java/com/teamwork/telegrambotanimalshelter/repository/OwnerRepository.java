package com.teamwork.telegrambotanimalshelter.repository;

import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    boolean existsOwnerByChatId(Long id);
    Owner getOwnerByChatId(Long id);
}
