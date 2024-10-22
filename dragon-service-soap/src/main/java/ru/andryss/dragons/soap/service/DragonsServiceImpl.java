package ru.andryss.dragons.soap.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.andryss.dragons.soap.entity.CaveEntity;
import ru.andryss.dragons.soap.entity.DragonEntity;
import ru.andryss.dragons.soap.exception.NotFoundException;
import ru.andryss.dragons.soap.gen.CoordinatesDto;
import ru.andryss.dragons.soap.gen.DragonCaveDto;
import ru.andryss.dragons.soap.gen.DragonDto;
import ru.andryss.dragons.soap.repository.CaveRepository;
import ru.andryss.dragons.soap.repository.DragonsRepository;

@SuppressWarnings("DuplicatedCode")
@Service
@RequiredArgsConstructor
public class DragonsServiceImpl implements DragonsService {

    private final DragonsRepository dragonsRepository;
    private final CaveRepository caveRepository;

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

    private static DragonDto mapToDto(DragonEntity dragon, @Nullable CaveEntity cave) {
        DragonDto dto = new DragonDto();
        dto.setId(dragon.getId());
        dto.setName(dragon.getName());

        CoordinatesDto coords = new CoordinatesDto();
        coords.setX(dragon.getX());
        coords.setY(dragon.getY());
        dto.setCoordinates(coords);

        dto.setCreationDate(toXmlGregorialCalendar(dragon.getCreationDate()));
        dto.setAge(dragon.getAge());
        dto.setDescription(dragon.getDescription());
        dto.setSpeaking(dragon.isSpeaking());
        dto.setColor(dragon.getColor());

        if (cave != null) {
            DragonCaveDto dragonCave = new DragonCaveDto();
            dragonCave.setId(cave.getId());
            dragonCave.setNumberOfTreasures(cave.getNumberOfTreasures());
            dto.setCave(dragonCave);
        }
        return dto;
    }

    private static XMLGregorianCalendar toXmlGregorialCalendar(Instant instant) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);

        XMLGregorianCalendar calendar = DatatypeFactory.newDefaultInstance().newXMLGregorianCalendar();
        calendar.setYear(dateTime.getYear());
        calendar.setMonth(dateTime.getMonthValue());
        calendar.setDay(dateTime.getDayOfMonth());
        calendar.setHour(dateTime.getHour());
        calendar.setMinute(dateTime.getMinute());
        calendar.setSecond(dateTime.getSecond());

        return calendar;
    }
}
