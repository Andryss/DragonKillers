package ru.andryss.dragons.service;

import java.util.List;

import org.springframework.lang.Nullable;
import ru.andryss.dragons.model.Color;
import ru.andryss.dragons.model.DescriptionInfo;
import ru.andryss.dragons.model.DragonDto;
import ru.andryss.dragons.model.SearchDragonInfo;

public interface DragonsService {
    DragonDto createDragon(String name, Double x, Float y, @Nullable Integer age, String description, Boolean speaking,
                           Color color, @Nullable Float caveNumberOfTreasures);

    DragonDto getDragonById(Integer id);

    DragonDto updateDragon(Integer id, String name, Double x, Float y, @Nullable Integer age, String description,
                           Boolean speaking, Color color, @Nullable Float caveNumberOfTreasures);

    void deleteDragon(Integer id);

    Integer countByColor(Color color);

    List<DescriptionInfo> groupByDescription();

    List<DragonDto> getWithGreaterCave(Float numberOfTreasures);

    List<DragonDto> search(SearchDragonInfo searchDragonInfo);
}
