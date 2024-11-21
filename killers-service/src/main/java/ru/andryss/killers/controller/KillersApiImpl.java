package ru.andryss.killers.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.andryss.killers.api.KillersApi;
import ru.andryss.killers.ejb.service.RemoteKillerService;
import ru.andryss.killers.model.KillerTeamDto;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KillersApiImpl implements KillersApi {

    private final RemoteKillerService killerService;

    @Override
    public KillerTeamDto createKillerTeam(Integer teamId, String teamName, Integer teamSize, Integer startCaveId) {
        log.info("Create killer team id {} name {} size {} caveId {}", teamId, teamName, teamSize, startCaveId);
        return killerService.createTeam(teamId, teamName, teamSize, startCaveId);
    }

    @Override
    public KillerTeamDto moveKillerTeam(Integer teamId, Integer caveId) {
        log.info("Move killer team id {} caveId {}", teamId, caveId);
        return killerService.moveTeam(teamId, caveId);
    }
}
