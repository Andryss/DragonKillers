package ru.andryss.dragons.service;

import org.springframework.lang.Nullable;
import ru.andryss.dragons.model.Color;
import ru.andryss.dragons.model.DragonDto;

public interface DragonsService {
    DragonDto createDragon(String name, Double x, Float y, @Nullable Integer age, String description, Boolean speaking,
                           Color color, @Nullable Float caveNumberOfTreasures);

    DragonDto getDragonById(Integer id);

    DragonDto updateDragon(Integer id, String name, Double x, Float y, @Nullable Integer age, String description,
                           Boolean speaking, Color color, @Nullable Float caveNumberOfTreasures);

    DragonDto deleteDragon(Integer id);
}
