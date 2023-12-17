package jznv.data;

import lombok.Getter;

@Getter
public enum DataType {
    USE_VK("Use VK"),
    GENDER("GenderState");

    private final String name;

    DataType(String name) {
        this.name = name;
    }
}