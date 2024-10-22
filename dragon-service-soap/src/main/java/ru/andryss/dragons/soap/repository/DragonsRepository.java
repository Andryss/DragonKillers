package ru.andryss.dragons.soap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.dragons.soap.entity.DragonEntity;

public interface DragonsRepository extends JpaRepository<DragonEntity, Integer> {
}
