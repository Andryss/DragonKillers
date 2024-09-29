package ru.andryss.dragons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.dragons.entity.CaveEntity;

public interface CaveRepository extends JpaRepository<CaveEntity, Integer> {
}
