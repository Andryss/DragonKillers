package ru.andryss.dragons.service;

import ru.andryss.dragons.model.Color;
import ru.andryss.dragons.model.DragonDto;

public interface DragonsService {
    DragonDto createDragon(String name, Double coordinatesX, Float coordinatesY, Integer age, String description,
                           Boolean speaking, Color color, Float caveNumberOfTreasures);
}
