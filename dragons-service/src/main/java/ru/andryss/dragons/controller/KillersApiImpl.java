package ru.andryss.dragons.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.andryss.dragons.api.KillersApi;
import ru.andryss.dragons.model.CreateKillerTeamRequest;
import ru.andryss.dragons.model.KillerTeamDto;
import ru.andryss.dragons.model.KillerTeamDtoList;
import ru.andryss.dragons.service.KillerService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KillersApiImpl implements KillersApi {

    private final KillerService killerService;

    @Override
    public KillerTeamDto createKillerTeam(CreateKillerTeamRequest request) {
        log.info("Create killer team request {}", request);
        return killerService.createTeam(request.getId(), request.getName(), request.getSize(), request.getCaveId());
    }

    @Override
    public KillerTeamDto getKillerTeam(Integer id) {
        log.info("Get killer team id {}", id);
        return killerService.getById(id);
    }

    @Override
    public KillerTeamDtoList getKillerTeams() {
        log.info("Get killer teams");
        List<KillerTeamDto> teams = killerService.getTeams();
        return new KillerTeamDtoList().teams(teams);
    }

    @Override
    public KillerTeamDto updateKillerTeam(Integer id, CreateKillerTeamRequest request) {
        log.info("Update killer team {} request {}", id, request);
        return killerService.updateById(id, request.getName(), request.getSize(), request.getCaveId());
    }

    @Override
    public void deleteKillerTeam(Integer id) {
        log.info("Delete killer team id {}", id);
        killerService.deleteById(id);
    }
}
