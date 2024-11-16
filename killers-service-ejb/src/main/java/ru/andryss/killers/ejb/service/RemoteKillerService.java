package ru.andryss.killers.ejb.service;

import jakarta.ejb.Remote;
import ru.andryss.killers.model.KillerTeamDto;

@Remote
public interface RemoteKillerService {
    KillerTeamDto createTeam(Integer teamId, String teamName, Integer teamSize, Integer startCaveId);

    KillerTeamDto moveTeam(Integer teamId, Integer caveId);
}
