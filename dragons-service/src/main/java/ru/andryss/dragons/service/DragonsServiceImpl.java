package ru.andryss.dragons.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.andryss.dragons.entity.CaveEntity;
import ru.andryss.dragons.entity.DragonEntity;
import ru.andryss.dragons.exception.NotFoundException;
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
    public DragonDto createDragon(String name, Double x, Float y, Integer age,
                                  String description, Boolean speaking, Color color,
                                  @Nullable Float caveNumberOfTreasures) {
        CaveEntity cave = null;
        if (caveNumberOfTreasures != null) {
            cave = new CaveEntity();
            cave.setNumberOfTreasures(caveNumberOfTreasures);

            caveRepository.save(cave);
        }

        DragonEntity dragon = new DragonEntity();
        dragon.setName(name);
        dragon.setX(x);
        dragon.setY(y);
        dragon.setCreationDate(Instant.now());
        dragon.setAge(age);
        dragon.setDescription(description);
        dragon.setSpeaking(speaking);
        dragon.setColor(color);
        if (cave != null) {
            dragon.setCaveId(cave.getId());
        }

        dragonsRepository.save(dragon);

        return mapToDto(dragon, cave);
    }

    @Override
    public DragonDto getDragonById(Integer id) {
        DragonEntity dragon = dragonsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("dragon %s".formatted(id)));

        CaveEntity cave = null;
        if (dragon.getCaveId() != null) {
            cave = caveRepository.findById(dragon.getCaveId())
                    .orElseThrow(() -> new NotFoundException("cave %s".formatted(dragon.getCaveId())));
        }

        return mapToDto(dragon, cave);
    }

    @Override
    public DragonDto updateDragon(Integer id, String name, Double x, Float y, @Nullable Integer age, String description,
                                  Boolean speaking, Color color, @Nullable Float caveNumberOfTreasures) {
        DragonEntity dragon = dragonsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("dragon %s".formatted(id)));

        dragon.setName(name);
        dragon.setX(x);
        dragon.setY(y);
        dragon.setAge(age);
        dragon.setDescription(description);
        dragon.setSpeaking(speaking);
        dragon.setColor(color);

        CaveEntity cave = null;
        if (caveNumberOfTreasures == null) {
            dragon.setCaveId(null);
        } else {
            if (dragon.getCaveId() != null) {
                cave = caveRepository.findById(dragon.getCaveId())
                        .orElseThrow(() -> new NotFoundException("cave %s".formatted(dragon.getCaveId())));
            } else {
                cave = new CaveEntity();
            }
            cave.setNumberOfTreasures(caveNumberOfTreasures);
            caveRepository.save(cave);
        }

        dragonsRepository.save(dragon);

        return mapToDto(dragon, cave);
    }

    @Override
    public DragonDto deleteDragon(Integer id) {
        DragonEntity dragon = dragonsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("dragon %s".formatted(id)));

        CaveEntity cave = null;
        if (dragon.getCaveId() != null) {
            cave = caveRepository.findById(dragon.getCaveId())
                    .orElseThrow(() -> new NotFoundException("cave %s".formatted(dragon.getCaveId())));
            caveRepository.delete(cave);
        }

        dragonsRepository.delete(dragon);

        return mapToDto(dragon, cave);
    }

    private static DragonDto mapToDto(DragonEntity dragon, @Nullable CaveEntity cave) {
        DragonDto dto = new DragonDto()
                .id(dragon.getId())
                .name(dragon.getName())
                .coordinates(new CoordinatesDto()
                        .x(dragon.getX())
                        .y(dragon.getY()))
                .creationDate(LocalDate.ofInstant(dragon.getCreationDate(), ZoneOffset.UTC))
                .age(dragon.getAge())
                .description(dragon.getDescription())
                .speaking(dragon.isSpeaking())
                .color(dragon.getColor());
        if (cave != null) {
            dto.cave(new DragonCaveDto()
                    .id(cave.getId())
                    .numberOfTreasures(cave.getNumberOfTreasures()));
        }
        return dto;
    }
}
