package ru.andryss.dragons.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.andryss.dragons.api.DragonsApi;
import ru.andryss.dragons.model.Color;
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
    public DragonDto createDragon(CreateDragonRequest createDragonRequest) {
        return dragonsService.createDragon(
                createDragonRequest.getName(),
                createDragonRequest.getCoordinates().getX(),
                createDragonRequest.getCoordinates().getY(),
                createDragonRequest.getAge(),
                createDragonRequest.getDescription(),
                createDragonRequest.getSpeaking(),
                createDragonRequest.getColor(),
                createDragonRequest.getCave().getNumberOfTreasures()
        );
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
    public DragonDto deleteDragon(Integer id) {
        return null;
    }

    @Override
    public DragonDto getDragon(Integer id) {
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

    @Override
    public DragonDto updateDragon(Integer id, CreateDragonRequest body) {
        return null;
    }
}
