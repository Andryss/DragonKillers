package ru.andryss.dragons.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.andryss.dragons.api.DragonsApi;
import ru.andryss.dragons.model.Color;
import ru.andryss.dragons.model.CreateDragonCave;
import ru.andryss.dragons.model.CreateDragonRequest;
import ru.andryss.dragons.model.DragonCaveDto;
import ru.andryss.dragons.model.DragonDto;
import ru.andryss.dragons.model.DragonsList;
import ru.andryss.dragons.model.GroupByDescriptionResponse;
import ru.andryss.dragons.model.SearchDragonInfo;
import ru.andryss.dragons.service.DragonsService;

@RestController
@RequiredArgsConstructor
public class DragonsApiImpl implements DragonsApi {

    private final DragonsService dragonsService;

    @Override
    public DragonDto createDragon(CreateDragonRequest request) {
        CreateDragonCave cave = request.getCave();
        return dragonsService.createDragon(
                request.getName(),
                request.getCoordinates().getX(),
                request.getCoordinates().getY(),
                request.getAge(),
                request.getDescription(),
                request.getSpeaking(),
                request.getColor(),
                (cave == null ? null : cave.getNumberOfTreasures())
        );
    }

    @Override
    public DragonDto getDragon(Integer id) {
        return dragonsService.getDragonById(id);
    }

    @Override
    public DragonDto updateDragon(Integer id, CreateDragonRequest request) {
        CreateDragonCave cave = request.getCave();
        return dragonsService.updateDragon(
                id,
                request.getName(),
                request.getCoordinates().getX(),
                request.getCoordinates().getY(),
                request.getAge(),
                request.getDescription(),
                request.getSpeaking(),
                request.getColor(),
                (cave == null ? null : cave.getNumberOfTreasures())
        );
    }

    @Override
    public DragonDto deleteDragon(Integer id) {
        return dragonsService.deleteDragon(id);
    }

    @Override
    public Integer countDragonsByColor(Color color) {
        return null;
    }

    @Override
    public DragonsList countDragonsWithGreaterCave(DragonCaveDto dragonCaveDto) {
        return null;
    }

    @Override
    public GroupByDescriptionResponse groupDragonsByDescription() {
        return null;
    }

    @Override
    public DragonsList searchDragons(SearchDragonInfo searchDragonInfo) {
        return null;
    }
}
