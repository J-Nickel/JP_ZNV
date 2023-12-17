package jznv.gui;

import javafx.scene.control.ToggleButton;
import jznv.data.DataType;
import lombok.Getter;

@Getter
public class ExtendedToggle extends ToggleButton {
    private final DataType type;

    public ExtendedToggle(DataType type) {
        this.type = type;
        setText(type.getName());
    }
}
