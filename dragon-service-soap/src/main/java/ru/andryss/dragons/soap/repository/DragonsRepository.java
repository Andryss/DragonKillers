package ru.andryss.dragons.soap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.dragons.soap.entity.DragonEntity;
import ru.andryss.dragons.soap.gen.Color;

public interface DragonsRepository extends JpaRepository<DragonEntity, Integer> {
    int countByColor(Color color);
}
