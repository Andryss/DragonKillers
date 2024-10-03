package ru.andryss.dragons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.dragons.entity.KillerTeamEntity;

public interface KillerTeamRepository extends JpaRepository<KillerTeamEntity, Integer> {
}
