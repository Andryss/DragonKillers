package ru.andryss.dragons.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.andryss.dragons.entity.CaveEntity;
import ru.andryss.dragons.entity.KillerTeamEntity;
import ru.andryss.dragons.exception.ConflictException;
import ru.andryss.dragons.exception.NotFoundException;
import ru.andryss.dragons.model.KillerTeamDto;
import ru.andryss.dragons.repository.CaveRepository;
import ru.andryss.dragons.repository.KillerTeamRepository;

@Service
@RequiredArgsConstructor
public class KillerServiceImpl implements KillerService {

    private final KillerTeamRepository killerTeamRepository;
    private final CaveRepository caveRepository;

    @Override
    public KillerTeamDto createTeam(Integer id, String name, Integer size, Integer caveId) {
        if (killerTeamRepository.findById(id).isPresent()) {
            throw new ConflictException("killer team with id %s exist".formatted(id));
        }
        CaveEntity cave = caveRepository.findById(caveId)
                .orElseThrow(() -> new NotFoundException("cave %s not found".formatted(caveId)));

        KillerTeamEntity team = new KillerTeamEntity();
        team.setId(id);
        team.setName(name);
        team.setSize(size);
        team.setCaveEntity(cave);

        killerTeamRepository.save(team);

        return mapToDto(team);
    }

    @Override
    public KillerTeamDto getById(Integer id) {
        KillerTeamEntity team = killerTeamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("killer team %s does not exist".formatted(id)));

        return mapToDto(team);
    }

    @Override
    public KillerTeamDto updateById(Integer id, String name, Integer size, Integer caveId) {
        KillerTeamEntity team = killerTeamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("killer team %s does not exist".formatted(id)));

        CaveEntity cave = caveRepository.findById(caveId)
                .orElseThrow(() -> new NotFoundException("cave %s not found".formatted(caveId)));

        team.setName(name);
        team.setSize(size);
        team.setCaveEntity(cave);

        killerTeamRepository.save(team);

        return mapToDto(team);
    }

    @Override
    public void deleteById(Integer id) {
        Optional<KillerTeamEntity> optionalTeam = killerTeamRepository.findById(id);

        if (optionalTeam.isEmpty()) {
            return;
        }

        KillerTeamEntity team = optionalTeam.get();
        killerTeamRepository.delete(team);
    }

    @Override
    public List<KillerTeamDto> getTeams() {
        List<KillerTeamEntity> entities = killerTeamRepository.findAll(Pageable.ofSize(500)).getContent();

        List<KillerTeamDto> teams = new ArrayList<>(entities.size());
        for (KillerTeamEntity entity : entities) {
            teams.add(mapToDto(entity));
        }

        return teams;
    }

    private static KillerTeamDto mapToDto(KillerTeamEntity team) {
        return new KillerTeamDto()
                .id(team.getId())
                .name(team.getName())
                .size(team.getSize())
                .caveId(team.getCaveEntity().getId());
    }
}
