package ru.andryss.dragons.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import ru.andryss.dragons.model.Color;

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
    Color color;
    Integer caveId;
}
