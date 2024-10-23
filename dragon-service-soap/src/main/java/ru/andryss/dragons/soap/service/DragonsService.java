package ru.andryss.dragons.soap.service;


import ru.andryss.dragons.soap.gen.Color;

public interface DragonsService {
    Integer countByColor(Color color);
}
