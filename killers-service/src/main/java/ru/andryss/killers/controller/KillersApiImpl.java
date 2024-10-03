package ru.andryss.killers.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.andryss.killers.api.KillersApi;
import ru.andryss.killers.model.KillerTeamDto;
import ru.andryss.killers.service.KillerService;

@RestController
@RequiredArgsConstructor
public class KillersApiImpl implements KillersApi {

    private final KillerService killerService;

    @Override
    public KillerTeamDto createKillerTeam(Integer teamId, String teamName, Integer teamSize, Integer startCaveId) {
        return killerService.createTeam(teamId, teamName, teamSize, startCaveId);
    }

    @Override
    public KillerTeamDto moveKillerTeam(Integer teamId, Integer caveId) {
        return killerService.moveTeam(teamId, caveId);
    }
}
