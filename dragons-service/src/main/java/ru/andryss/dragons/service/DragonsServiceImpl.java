package ru.andryss.dragons.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
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
import ru.andryss.dragons.model.StringFilter;
import ru.andryss.dragons.repository.CaveRepository;
import ru.andryss.dragons.repository.DragonsRepository;

@SuppressWarnings("DuplicatedCode")
@Service
@RequiredArgsConstructor
public class DragonsServiceImpl implements DragonsService {

    private final DragonsRepository dragonsRepository;
    private final CaveRepository caveRepository;
    private final EntityManager entityManager;

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
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<DragonEntity> query = cb.createQuery(DragonEntity.class);
        Root<DragonEntity> root = query.from(DragonEntity.class);
        query.where(getPredicates(cb, root, info));
        query.orderBy(getOrders(cb, root, info));

        TypedQuery<DragonEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(info.getPageNumber() * info.getPageSize());
        typedQuery.setMaxResults(info.getPageSize());

        List<DragonEntity> dragons = typedQuery.getResultList();
        return mapListToDto(dragons);
    }

    private static Predicate[] getPredicates(CriteriaBuilder cb, Root<DragonEntity> root, SearchDragonInfo info) {
        List<Predicate> predicates = new ArrayList<>();

        DragonFilter filter = info.getFilter();
        if (filter == null) return new Predicate[0];

        addIntFilterPredicates(cb, predicates, root, filter.getId(), "id");
        addStringFilterPredicates(cb, predicates, root, filter.getName(), "name");

        CoordinatesFilter coordinatesFilter = filter.getCoordinates();
        if (coordinatesFilter != null) {
            addFloatFilterPredicates(cb, predicates, root, coordinatesFilter.getX(), "x");
            addFloatFilterPredicates(cb, predicates, root, coordinatesFilter.getY(), "y");
        }

        addDateFilterPredicates(cb, predicates, root, filter.getCreationDate(), "creationDate");
        addIntFilterPredicates(cb, predicates, root, filter.getAge(), "age");
        addStringFilterPredicates(cb, predicates, root, filter.getDescription(), "description");
        addBooleanFilterPredicates(cb, predicates, root, filter.getSpeaking(), "speaking");
        addStringFilterPredicates(cb, predicates, root, filter.getColor(), "color");

        DragonCaveFilter caveFilter = filter.getCave();
        if (caveFilter != null) {
            addIntFilterPredicates(cb, predicates, root, caveFilter.getId(), "id");
            // addFloatFilterPredicates(cb, predicates, root, caveFilter.getNumberOfTreasures(), "numberOfTreasures");
        }

        return predicates.toArray(new Predicate[0]);
    }

    @SuppressWarnings("SameParameterValue")
    private static void addBooleanFilterPredicates(CriteriaBuilder cb, List<Predicate> predicates, Root<DragonEntity> root,
                                                   BooleanFilter filter, String property) {
        if (filter == null) return;
        if (filter.getEq() != null) predicates.add(cb.equal(root.get(property), filter.getEq()));
    }

    @SuppressWarnings("SameParameterValue")
    private static void addDateFilterPredicates(CriteriaBuilder cb, List<Predicate> predicates, Root<DragonEntity> root,
                                                DateFilter filter, String property) {
        if (filter == null) return;
        if (filter.getEq() != null) predicates.add(cb.equal(root.get(property), filter.getEq()));
        if (filter.getGr() != null) predicates.add(cb.greaterThan(root.get(property), filter.getGr()));
        if (filter.getLw() != null) predicates.add(cb.lessThan(root.get(property), filter.getLw()));
    }

    private static void addFloatFilterPredicates(CriteriaBuilder cb, List<Predicate> predicates, Root<DragonEntity> root,
                                  FloatFilter filter, String property) {
        if (filter == null) return;
        if (filter.getEq() != null) predicates.add(cb.equal(root.get(property), filter.getEq()));
        if (filter.getGr() != null) predicates.add(cb.greaterThan(root.get(property), filter.getGr()));
        if (filter.getLw() != null) predicates.add(cb.lessThan(root.get(property), filter.getLw()));
    }

    private static void addStringFilterPredicates(CriteriaBuilder cb, List<Predicate> predicates, Root<DragonEntity> root,
                                  StringFilter filter, String property) {
        if (filter == null) return;
        if (filter.getEq() != null && !filter.getEq().isEmpty()) predicates.add(cb.equal(root.get(property), filter.getEq()));
    }

    private static void addIntFilterPredicates(CriteriaBuilder cb, List<Predicate> predicates, Root<DragonEntity> root,
                                  IntFilter filter, String property) {
        if (filter == null) return;
        if (filter.getEq() != null) predicates.add(cb.equal(root.get(property), filter.getEq()));
        if (filter.getGr() != null) predicates.add(cb.greaterThan(root.get(property), filter.getGr()));
        if (filter.getLw() != null) predicates.add(cb.lessThan(root.get(property), filter.getLw()));
    }

    private static Order[] getOrders(CriteriaBuilder cb, Root<DragonEntity> root, SearchDragonInfo info) {
        switch (info.getSortOrder()) {
            case ASC -> {
                return new Order[]{ cb.asc(root.get(info.getSortBy())) };
            }
            case DESC -> {
                return new Order[]{ cb.desc(root.get(info.getSortBy())) };
            }
        }
        return new Order[0];
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
