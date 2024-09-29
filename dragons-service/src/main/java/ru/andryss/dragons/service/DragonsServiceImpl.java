package ru.andryss.dragons.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.andryss.dragons.entity.CaveEntity;
import ru.andryss.dragons.entity.DragonEntity;
import ru.andryss.dragons.model.Color;
import ru.andryss.dragons.model.CoordinatesDto;
import ru.andryss.dragons.model.DragonCaveDto;
import ru.andryss.dragons.model.DragonDto;
import ru.andryss.dragons.repository.CaveRepository;
import ru.andryss.dragons.repository.DragonsRepository;

@Service
@RequiredArgsConstructor
public class DragonsServiceImpl implements DragonsService {

    private final DragonsRepository dragonsRepository;
    private final CaveRepository caveRepository;

    @Override
    public DragonDto createDragon(String name, Double coordinatesX, Float coordinatesY, Integer age,
                                  String description, Boolean speaking, Color color, Float caveNumberOfTreasures) {
        CaveEntity cave = new CaveEntity();
        cave.setNumberOfTreasures(caveNumberOfTreasures);

        caveRepository.save(cave);

        DragonEntity dragon = new DragonEntity();
        dragon.setName(name);
        dragon.setX(coordinatesX);
        dragon.setY(coordinatesY);
        dragon.setCreationDate(Instant.now());
        dragon.setAge(age);
        dragon.setDescription(description);
        dragon.setSpeaking(speaking);
        dragon.setColor(color);
        dragon.setCaveId(cave.getId());

        dragonsRepository.save(dragon);

        return new DragonDto()
                .id(dragon.getId())
                .name(dragon.getName())
                .coordinates(new CoordinatesDto()
                        .x(dragon.getX())
                        .y(dragon.getY()))
                .creationDate(LocalDate.ofInstant(dragon.getCreationDate(), ZoneOffset.UTC))
                .age(dragon.getAge())
                .description(dragon.getDescription())
                .speaking(dragon.isSpeaking())
                .color(dragon.getColor())
                .cave(new DragonCaveDto()
                        .id(cave.getId())
                        .numberOfTreasures(cave.getNumberOfTreasures()));
    }
}
