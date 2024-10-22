package ru.andryss.dragons.soap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.dragons.soap.entity.CaveEntity;

public interface CaveRepository extends JpaRepository<CaveEntity, Integer> {
}
