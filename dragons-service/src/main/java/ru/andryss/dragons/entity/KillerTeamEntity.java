package ru.andryss.dragons.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class KillerTeamEntity {
    @Id
    int id;
    String name;
    int size;
    @ManyToOne
    CaveEntity caveEntity;
}
