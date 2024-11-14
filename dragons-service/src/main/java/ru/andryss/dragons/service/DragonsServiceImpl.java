package ru.andryss.dragons.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
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
import ru.andryss.dragons.model.DragonDto;
import ru.andryss.dragons.model.DragonFilter;
import ru.andryss.dragons.model.FloatFilter;
import ru.andryss.dragons.model.IntFilter;
import ru.andryss.dragons.model.SearchDragonInfo;
import ru.andryss.dragons.model.SearchDragonInfo.SortOrderEnum;
import ru.andryss.dragons.model.StringFilter;
import ru.andryss.dragons.repository.DragonsRepository;

@SuppressWarnings("DuplicatedCode")
@Service
@RequiredArgsConstructor
public class DragonsServiceImpl implements DragonsService {

    private final DragonsRepository dragonsRepository;

    @Override
    public DragonDto createDragon(String name, Double x, Float y, Integer age,
                                  String description, Boolean speaking, Color color,
                                  @Nullable Float caveNumberOfTreasures) {
        DragonEntity dragon = new DragonEntity();
        dragon.setName(name);
        dragon.setX(x);
        dragon.setY(y);
        dragon.setCreationDate(Instant.now());
        dragon.setAge(age);
        dragon.setDescription(description);
        dragon.setSpeaking(speaking);
        dragon.setColor(color);
        if (caveNumberOfTreasures != null) {
            CaveEntity cave = new CaveEntity();
            cave.setNumberOfTreasures(caveNumberOfTreasures);

            dragon.setCaveEntity(cave);
        }

        dragonsRepository.save(dragon);

        return mapToDto(dragon);
    }

    @Override
    public DragonDto getDragonById(Integer id) {
        DragonEntity dragon = dragonsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("dragon %s".formatted(id)));

        return mapToDto(dragon);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
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

        if (caveNumberOfTreasures == null) {
            dragon.setCaveEntity(null);
        } else {
            CaveEntity cave = dragon.getCaveEntity();
            if (cave == null) {
                cave = new CaveEntity();
            }
            cave.setNumberOfTreasures(caveNumberOfTreasures);
            dragon.setCaveEntity(cave);
        }

        dragonsRepository.save(dragon);

        return mapToDto(dragon);
    }

    @Override
    public void deleteDragon(Integer id) {
        dragonsRepository.deleteById(id);
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
        if (info == null) {
            return (root, query, builder) -> null;
        }
        return Specification.allOf(
                intFilterSpec(info.getId(), "id"),
                stringFilterSpec(info.getName(), "name"),
                coordsFilterSpec(info.getCoordinates()),
                dateFilterSpec(info.getCreationDate(), "creationDate"),
                intFilterSpec(info.getAge(), "age"),
                stringFilterSpec(info.getDescription(), "description"),
                booleanFilterSpec(info.getSpeaking(), "speaking"),
                stringFilterSpec(info.getColor(), "color")
        );
    }

    private static Specification<DragonEntity> coordsFilterSpec(CoordinatesFilter filter) {
        return (filter == null ? null : Specification.allOf(
                floatFilterSpec(filter.getX(), "x"),
                floatFilterSpec(filter.getY(), "y")
        ));
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
        return foundDragons.stream().map(DragonsServiceImpl::mapToDto).toList();
    }

    private static DragonDto mapToDto(DragonEntity dragon) {
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
        CaveEntity cave = dragon.getCaveEntity();
        if (cave != null) {
            dto.cave(new DragonCaveDto()
                    .id(cave.getId())
                    .numberOfTreasures(cave.getNumberOfTreasures()));
        }
        return dto;
    }
}
