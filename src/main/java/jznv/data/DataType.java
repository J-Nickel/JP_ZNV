package jznv.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DataType {
    VK("Найдено в ВК"),
    Gender("Распределение по полу"),
    Age("Распределение по возрасту"),
    City("Распределение по городам"),
    Group("Распределение по группам"),
    ThemeAVG("Степень прохождения тем");

    private final String name;
}
