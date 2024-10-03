package ru.andryss.dragons.service;

import java.util.List;

import ru.andryss.dragons.model.KillerTeamDto;

public interface KillerService {
    KillerTeamDto createTeam(Integer id, String name, Integer size, Integer caveId);

    KillerTeamDto getById(Integer id);

    KillerTeamDto updateById(Integer id, String name, Integer size, Integer caveId);

    void deleteById(Integer id);

    List<KillerTeamDto> getTeams();
}
