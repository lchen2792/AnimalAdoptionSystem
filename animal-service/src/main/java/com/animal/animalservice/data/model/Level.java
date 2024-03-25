package com.animal.animalservice.data.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum Level implements Serializable {
    EXTREME_LOW(1),
    EXTRA_LOW(2),
    LOW(3),
    MEDIUM(4),
    HIGH(5),
    EXTRA_HIGH(6),
    EXTREME_HIGH(7);

    private int value;

    Level(int v) {
        this.value = v;
    }
}
