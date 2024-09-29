package ru.andryss.dragons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.dragons.entity.DragonEntity;

public interface DragonsRepository extends JpaRepository<DragonEntity, Integer> {
}
