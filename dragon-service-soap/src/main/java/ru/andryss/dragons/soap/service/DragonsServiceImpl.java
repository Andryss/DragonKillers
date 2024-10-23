package ru.andryss.dragons.soap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.andryss.dragons.soap.gen.Color;
import ru.andryss.dragons.soap.repository.DragonsRepository;

@Service
@RequiredArgsConstructor
public class DragonsServiceImpl implements DragonsService {

    private final DragonsRepository dragonsRepository;

    @Override
    public Integer countByColor(Color color) {
        return dragonsRepository.countByColor(color);
    }
}
