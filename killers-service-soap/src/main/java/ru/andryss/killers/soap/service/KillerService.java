package ru.andryss.killers.soap.service;


import ru.andryss.killers.model.KillerTeamDto;

public interface KillerService {
    KillerTeamDto createTeam(Integer teamId, String teamName, Integer teamSize, Integer startCaveId);

    KillerTeamDto moveTeam(Integer teamId, Integer caveId);
}
