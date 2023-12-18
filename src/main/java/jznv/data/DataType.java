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
    ThemeAVG("Степень прохождения тем"),
    TaskAVG_T5("Статистика по темам 0-5"),
    TaskAVG_T10("Статистика темам 5-10"),
    TaskAVG_T15("Статистика по темам 10-15");

    private final String name;
}
