package ru.andryss.dragons.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.andryss.dragons.entity.CaveEntity;
import ru.andryss.dragons.entity.DragonEntity;
import ru.andryss.dragons.exception.NotFoundException;
import ru.andryss.dragons.model.BooleanFilter;
import ru.andryss.dragons.model.Color;
import ru.andryss.dragons.model.CoordinatesDto;
import ru.andryss.dragons.model.CoordinatesFilter;
import ru.andryss.dragons.model.DateFilter;
import ru.andryss.dragons.model.DescriptionInfo;
import ru.andryss.dragons.model.DragonCaveDto;
import ru.andryss.dragons.model.DragonCaveFilter;
import ru.andryss.dragons.model.DragonDto;
import ru.andryss.dragons.model.DragonFilter;
import ru.andryss.dragons.model.FloatFilter;
import ru.andryss.dragons.model.IntFilter;
import ru.andryss.dragons.model.SearchDragonInfo;
import ru.andryss.dragons.model.SearchDragonInfo.SortOrderEnum;
import ru.andryss.dragons.model.StringFilter;
import ru.andryss.dragons.repository.CaveRepository;
import ru.andryss.dragons.repository.DragonsRepository;

@SuppressWarnings("DuplicatedCode")
@Service
@RequiredArgsConstructor
public class DragonsServiceImpl implements DragonsService {

    private final DragonsRepository dragonsRepository;
    private final CaveRepository caveRepository;

    @Override
    public DragonDto createDragon(String name, Double x, Float y, Integer age,
                                  String description, Boolean speaking, Color color,
                                  @Nullable Float caveNumberOfTreasures) {
        CaveEntity cave = null;
        if (caveNumberOfTreasures != null) {
            cave = new CaveEntity();
            cave.setNumberOfTreasures(caveNumberOfTreasures);

            caveRepository.save(cave);
        }

        DragonEntity dragon = new DragonEntity();
        dragon.setName(name);
        dragon.setX(x);
        dragon.setY(y);
        dragon.setCreationDate(Instant.now());
        dragon.setAge(age);
        dragon.setDescription(description);
        dragon.setSpeaking(speaking);
        dragon.setColor(color);
        if (cave != null) {
            dragon.setCaveId(cave.getId());
        }

        dragonsRepository.save(dragon);

        return mapToDto(dragon, cave);
    }

    @Override
    public DragonDto getDragonById(Integer id) {
        DragonEntity dragon = dragonsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("dragon %s".formatted(id)));

        CaveEntity cave = null;
        if (dragon.getCaveId() != null) {
            cave = caveRepository.findById(dragon.getCaveId())
                    .orElseThrow(() -> new NotFoundException("cave %s".formatted(dragon.getCaveId())));
        }

        return mapToDto(dragon, cave);
    }

    @Override
    public DragonDto updateDragon(Integer id, String name, Double x, Float y, @Nullable Integer age, String description,
                                  Boolean speaking, Color color, @Nullable Float caveNumberOfTreasures) {
        DragonEntity dragon = dragonsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("dragon %s".formatted(id)));

        dragon.setName(name);
        dragon.setX(x);
        dragon.setY(y);
        dragon.setAge(age);
        dragon.setDescription(description);
        dragon.setSpeaking(speaking);
        dragon.setColor(color);

        CaveEntity cave = null;
        if (caveNumberOfTreasures == null) {
            dragon.setCaveId(null);
        } else {
            if (dragon.getCaveId() != null) {
                cave = caveRepository.findById(dragon.getCaveId())
                        .orElseThrow(() -> new NotFoundException("cave %s".formatted(dragon.getCaveId())));
            } else {
                cave = new CaveEntity();
            }
            cave.setNumberOfTreasures(caveNumberOfTreasures);
            caveRepository.save(cave);
        }

        dragonsRepository.save(dragon);

        return mapToDto(dragon, cave);
    }

    @Override
    public void deleteDragon(Integer id) {
        Optional<DragonEntity> optionalDragon = dragonsRepository.findById(id);
        if (optionalDragon.isEmpty()) {
            return;
        }

        DragonEntity dragon = optionalDragon.get();

        if (dragon.getCaveId() != null) {
            caveRepository.deleteById(dragon.getCaveId());
        }

        dragonsRepository.delete(dragon);
    }

    @Override
    public Integer countByColor(Color color) {
        return dragonsRepository.countByColor(color);
    }

    @Override
    public List<DescriptionInfo> groupByDescription() {
        ArrayList<DescriptionInfo> infos = new ArrayList<>();

        List<String> descriptions = dragonsRepository.getAllDescriptions();
        for (String description : descriptions) {
            infos.add(new DescriptionInfo()
                    .description(description)
                    .count(dragonsRepository.countByDescription(description)));
        }

        return infos;
    }

    @Override
    public List<DragonDto> getWithGreaterCave(Float numberOfTreasures) {
        List<DragonEntity> foundDragons = dragonsRepository.findWithGreaterCave(numberOfTreasures);
        return mapListToDto(foundDragons);
    }

    @Override
    public List<DragonDto> search(SearchDragonInfo info) {
        PageRequest pageRequest = PageRequest.of(info.getPageNumber(), info.getPageSize(),
                toDirection(info.getSortOrder()), info.getSortBy());

        Page<DragonEntity> page = dragonsRepository.findAll(isSuitsFilter(info.getFilter()), pageRequest);

        return mapListToDto(page.stream().toList());
    }

    private Specification<DragonEntity> isSuitsFilter(DragonFilter info) {
        return Specification.allOf(
                intFilterSpec(info.getId(), "id"),
                stringFilterSpec(info.getName(), "name"),
                coordsFilterSpec(info.getCoordinates()),
                dateFilterSpec(info.getCreationDate(), "creationDate"),
                intFilterSpec(info.getAge(), "age"),
                stringFilterSpec(info.getDescription(), "description"),
                booleanFilterSpec(info.getSpeaking(), "speaking"),
                stringFilterSpec(info.getColor(), "color"),
                caveFilterSpec(info.getCave())
        );
    }

    private static Specification<DragonEntity> coordsFilterSpec(CoordinatesFilter filter) {
        return (filter == null ? null : Specification.allOf(
                floatFilterSpec(filter.getX(), "x"),
                floatFilterSpec(filter.getY(), "y")
        ));
    }

    private static Specification<DragonEntity> caveFilterSpec(DragonCaveFilter filter) {
        return (filter == null ? null :
                intFilterSpec(filter.getId(), "cave_id")
        );
    }

    private static Direction toDirection(SortOrderEnum order) {
        return (order == SortOrderEnum.ASC ? Direction.ASC : Direction.DESC);
    }

    @SuppressWarnings("SameParameterValue")
    private static Specification<DragonEntity> booleanFilterSpec(BooleanFilter filter, String prop) {
        return (filter == null ? null :
                (root, query, builder) -> (filter.getEq() == null ? null : builder.equal(root.get(prop), filter.getEq()))
        );
    }

    @SuppressWarnings("SameParameterValue")
    private static Specification<DragonEntity> dateFilterSpec(DateFilter filter, String prop) {
        return (filter == null ? null : Specification.allOf(
                (root, query, builder) -> (filter.getEq() == null ? null : builder.equal(root.get(prop), filter.getEq())),
                (root, query, builder) -> (filter.getGr() == null ? null : builder.greaterThan(root.get(prop), filter.getGr())),
                (root, query, builder) -> (filter.getLw() == null ? null : builder.lessThan(root.get(prop), filter.getLw()))
        ));
    }

    private static Specification<DragonEntity> floatFilterSpec(FloatFilter filter, String prop) {
        return (filter == null ? null : Specification.allOf(
                (root, query, builder) -> (filter.getEq() == null ? null : builder.equal(root.get(prop), filter.getEq())),
                (root, query, builder) -> (filter.getGr() == null ? null : builder.greaterThan(root.get(prop), filter.getGr())),
                (root, query, builder) -> (filter.getLw() == null ? null : builder.lessThan(root.get(prop), filter.getLw()))
        ));
    }

    private static Specification<DragonEntity> stringFilterSpec(StringFilter filter, String prop) {
        return (filter == null ? null :
                (root, query, builder) -> (filter.getEq() == null ? null : builder.equal(root.get(prop), filter.getEq()))
        );
    }

    private static Specification<DragonEntity> intFilterSpec(IntFilter filter, String prop) {
        return (filter == null ? null : Specification.allOf(
                (root, query, builder) -> (filter.getEq() == null ? null : builder.equal(root.get(prop), filter.getEq())),
                (root, query, builder) -> (filter.getGr() == null ? null : builder.greaterThan(root.get(prop), filter.getGr())),
                (root, query, builder) -> (filter.getLw() == null ? null : builder.lessThan(root.get(prop), filter.getLw()))
        ));
    }

    private List<DragonDto> mapListToDto(List<DragonEntity> foundDragons) {
        List<DragonDto> dragons = new ArrayList<>();
        for (DragonEntity dragon : foundDragons) {
            Integer caveId = dragon.getCaveId();
            CaveEntity cave = (caveId == null ? null : caveRepository.findById(caveId).orElse(null));
            dragons.add(mapToDto(dragon, cave));
        }
        return dragons;
    }

    private static DragonDto mapToDto(DragonEntity dragon, @Nullable CaveEntity cave) {
        DragonDto dto = new DragonDto()
                .id(dragon.getId())
                .name(dragon.getName())
                .coordinates(new CoordinatesDto()
                        .x(dragon.getX())
                        .y(dragon.getY()))
                .creationDate(OffsetDateTime.ofInstant(dragon.getCreationDate(), ZoneOffset.UTC))
                .age(dragon.getAge())
                .description(dragon.getDescription())
                .speaking(dragon.isSpeaking())
                .color(dragon.getColor());
        if (cave != null) {
            dto.cave(new DragonCaveDto()
                    .id(cave.getId())
                    .numberOfTreasures(cave.getNumberOfTreasures()));
        }
        return dto;
    }
}
