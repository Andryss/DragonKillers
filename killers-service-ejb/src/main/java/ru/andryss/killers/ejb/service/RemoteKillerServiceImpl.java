package ru.andryss.killers.ejb.service;

import jakarta.ejb.Stateless;
import ru.andryss.killers.model.KillerTeamDto;

@Stateless
public class RemoteKillerServiceImpl implements RemoteKillerService {
    @Override
    public KillerTeamDto createTeam(Integer teamId, String teamName, Integer teamSize, Integer startCaveId) {
        return new KillerTeamDto();
    }

    @Override
    public KillerTeamDto moveTeam(Integer teamId, Integer caveId) {
        return new KillerTeamDto();
    }
}
