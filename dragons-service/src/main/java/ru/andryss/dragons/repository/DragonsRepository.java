package ru.andryss.dragons.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.andryss.dragons.entity.DragonEntity;
import ru.andryss.dragons.model.Color;

public interface DragonsRepository extends JpaRepository<DragonEntity, Integer> {
    int countByColor(Color color);
    @Query("select distinct(d.description) from DragonEntity d")
    List<String> getAllDescriptions();
    int countByDescription(String description);
    @Query("""
        select d from DragonEntity d
            join CaveEntity c on d.caveId = c.id
            where c.numberOfTreasures > :treasures""")
    List<DragonEntity> findWithGreaterCave(@Param("treasures") Float numberOfThreasures);
}
