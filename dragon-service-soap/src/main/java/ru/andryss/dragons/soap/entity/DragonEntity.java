package ru.andryss.dragons.soap.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import ru.andryss.dragons.soap.gen.Color;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class DragonEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    int id;
    String name;
    double x;
    float y;
    Instant creationDate;
    Integer age;
    String description;
    boolean speaking;
    @Enumerated(EnumType.STRING)
    Color color;
    Integer caveId;
}
