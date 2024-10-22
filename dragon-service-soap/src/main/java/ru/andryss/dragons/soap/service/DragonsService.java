package ru.andryss.dragons.soap.service;


import ru.andryss.dragons.soap.gen.DragonDto;

public interface DragonsService {
    DragonDto getDragonById(Integer id);
}
