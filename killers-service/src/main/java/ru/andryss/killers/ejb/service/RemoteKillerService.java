package ru.andryss.killers.ejb.service;

import ru.andryss.killers.model.KillerTeamDto;

public interface RemoteKillerService {
    KillerTeamDto createTeam(Integer teamId, String teamName, Integer teamSize, Integer startCaveId);

    KillerTeamDto moveTeam(Integer teamId, Integer caveId);
}
