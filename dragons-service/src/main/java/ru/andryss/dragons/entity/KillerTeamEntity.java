package ru.andryss.dragons.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
    @OneToOne
    CaveEntity caveEntity;
}
