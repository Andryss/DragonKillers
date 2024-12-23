package ru.andryss.dragons.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.andryss.dragons.api.DragonsApi;
import ru.andryss.dragons.model.Color;
import ru.andryss.dragons.model.CreateDragonCave;
import ru.andryss.dragons.model.CreateDragonRequest;
import ru.andryss.dragons.model.DescriptionInfo;
import ru.andryss.dragons.model.DragonDto;
import ru.andryss.dragons.model.DragonsList;
import ru.andryss.dragons.model.GreaterCaveRequest;
import ru.andryss.dragons.model.GroupByDescriptionResponse;
import ru.andryss.dragons.model.SearchDragonInfo;
import ru.andryss.dragons.service.DragonsService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DragonsApiImpl implements DragonsApi {

    private final DragonsService dragonsService;

    @Override
    public DragonDto createDragon(CreateDragonRequest request) {
        log.info("Create dragon request {}", request);
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
        log.info("Get dragon {}", id);
        return dragonsService.getDragonById(id);
    }

    @Override
    public DragonDto updateDragon(Integer id, CreateDragonRequest request) {
        log.info("Update dragon {} request {}", id, request);
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
    public void deleteDragon(Integer id) {
        log.info("Delete dragon {}", id);
        dragonsService.deleteDragon(id);
    }

    @Override
    public Integer countDragonsByColor(Color color) {
        log.info("Count dragons by color {}", color);
        return dragonsService.countByColor(color);
    }

    @Override
    public DragonsList countDragonsWithGreaterCave(GreaterCaveRequest request) {
        log.info("Count dragons with cave greater then {}", request);
        List<DragonDto> dragons = dragonsService.getWithGreaterCave(request.getNumberOfTreasures());
        return new DragonsList().dragons(dragons);
    }

    @Override
    public GroupByDescriptionResponse groupDragonsByDescription() {
        log.info("Group dragons by description");
        List<DescriptionInfo> infos = dragonsService.groupByDescription();
        return new GroupByDescriptionResponse().descriptions(infos);
    }

    @Override
    public DragonsList searchDragons(SearchDragonInfo searchDragonInfo) {
        log.info("Search dragons request {}", searchDragonInfo);
        List<DragonDto> dragons = dragonsService.search(searchDragonInfo);
        return new DragonsList().dragons(dragons);
    }
}
