package ru.andryss.dragons.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class CaveEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    int id;
    float numberOfTreasures;
}
